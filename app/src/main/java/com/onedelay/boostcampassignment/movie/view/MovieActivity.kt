package com.onedelay.boostcampassignment.movie.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.like.LikeActivity
import com.onedelay.boostcampassignment.movie.caseProvider.present.MoviePresentCaseApi
import com.onedelay.boostcampassignment.movie.caseProvider.source.MovieSourceCaseApi
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import com.onedelay.boostcampassignment.movie.view.custom.EntireMovieLayout
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_movie.*
import javax.inject.Inject


internal class MovieActivity : DaggerAppCompatActivity(), MovieView {

    @Inject lateinit var presentCase: MoviePresentCaseApi

    @Inject lateinit var sourceCase: MovieSourceCaseApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        initializeListener()

        presentCase.onLifecycle(ActivityLifeCycleState.OnCreate())
    }

    override fun onDestroy() {
        presentCase.onLifecycle(ActivityLifeCycleState.OnDestroy())

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.menu_liked_movie -> {
                presentCase.onViewAction(MovieViewAction.Click.OptionMenuLike())

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun bindLooknFeel(looknFeel: MovieLooknFeel) {
        when(looknFeel) {
            is MovieLooknFeel.BindMovieRecyclerView -> bindMovieList(looknFeel)

            is MovieLooknFeel.BindMoreMovieRecyclerView -> bindMoreMovieList(looknFeel)

            is MovieLooknFeel.BindLikedMovieList -> updateLikedMovieList(looknFeel)

            is MovieLooknFeel.BindRemovedMovieItem -> removeMovieItem(looknFeel)
        }
    }

    override fun bindNavigation(navigation: MovieNavigation) {
        when(navigation) {
            is MovieNavigation.ToLikeActivity -> {
                startActivity(Intent(this@MovieActivity, LikeActivity::class.java))
            }

            is MovieNavigation.ToWebViewActivity -> {
                val intent = Intent(this@MovieActivity, WebViewActivity::class.java).apply {
                    putExtra(Constants.URL, navigation.movieLink)
                }

                startActivity(intent)
            }
        }
    }

    private fun bindMovieList(looknFeel: MovieLooknFeel.BindMovieRecyclerView) {
        custom_fl_entire_movie.setLooknFeel(EntireMovieLayout.LooknFeel(looknFeel.movieLooknFeelList))
    }

    private fun bindMoreMovieList(looknFeel: MovieLooknFeel.BindMoreMovieRecyclerView) {
        custom_fl_entire_movie.bindMoreMovieList(looknFeel.movieLooknFeelList)
    }

    private fun removeMovieItem(looknFeel: MovieLooknFeel.BindRemovedMovieItem) {
        custom_fl_entire_movie.removeMovieItem(looknFeel.movieItem)
    }

    private fun updateLikedMovieList(looknFeel: MovieLooknFeel.BindLikedMovieList) {
        custom_fl_entire_movie.updateLikedMovieList(looknFeel.likedMovieLooknFeelList)
    }

    private fun initializeListener() {
        custom_fl_entire_movie.setListener(object : EntireMovieLayout.Listener {
            override fun onMovieSearchRequested(movieName: String) {
                presentCase.onViewAction(MovieViewAction.Click.Search(movieName = movieName))
            }

            override fun onMovieClicked(looknFeel: MovieLayout.LooknFeel) {
                presentCase.onViewAction(MovieViewAction.Click.ItemElement(looknFeel.link))
            }

            override fun onLongClick(looknFeel: MovieLayout.LooknFeel) {
                showDialog(looknFeel)
            }

            override fun onLoadMoreRequested(start: Int) {
                presentCase.onViewAction(MovieViewAction.LoadMore(start = start))
            }
        })
    }

    private fun showDialog(item: MovieLayout.LooknFeel) {
        val dialogMessages = if(!item.starred) {
            arrayOf("삭제", "즐겨찾기")
        } else {
            arrayOf("삭제", "즐겨찾기 삭제")
        }

        AlertDialog.Builder(this).apply {
            setItems(
                    dialogMessages
            ) { _, which ->
                when(which) {
                    0 -> {
                        presentCase.onViewAction(MovieViewAction.Click.RemoveMovie(item))
                    }

                    1 -> {
                        presentCase.onViewAction(MovieViewAction.Click.LikeMovie(item))
                    }
                }
            }
        }.create().show()
    }

}
