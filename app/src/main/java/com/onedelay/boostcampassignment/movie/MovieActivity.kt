package com.onedelay.boostcampassignment.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.BaseActivity
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.like.LikeActivity
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import kotlinx.android.synthetic.main.activity_movie.*
import javax.inject.Inject


internal class MovieActivity : BaseActivity() {

    private val viewModel by lazy { createViewModel(MovieViewModel::class.java) }

    @Inject lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        initializeListener()

        initializeRecyclerView()

        subscribeLooknFeel()

        subscribeNavigation()

        viewModel.channel.accept(ActivityLifeCycleState.OnCreate())
    }

    override fun onDestroy() {
        viewModel.channel.accept(ActivityLifeCycleState.OnDestroy())

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.menu_liked_movie -> {
                viewModel.channel.accept(MovieViewAction.Click.OptionMenuLike())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeLooknFeel() {
        with(viewModel) {
            activityScopeCompositeDisposable.addAll(
                    channel.ofLooknFeel()
                            .doOnError { Log.d("MY_LOG", "${it.printStackTrace()}") }
                            .subscribe {
                                when(it) {
                                    is MovieLooknFeel.BindMovieRecyclerView -> bindMovieList(it)

                                    is MovieLooknFeel.BindMoreMovieRecyclerView -> bindMoreMovieList(it)

                                    is MovieLooknFeel.BindUpdatedMovieItem -> updateMovieItem(it)

                                    is MovieLooknFeel.BindRemovedMovieItem -> removeMovieItem(it)
                                }
                            }
            )
        }
    }

    private fun subscribeNavigation() {
        with(viewModel) {
            activityScopeCompositeDisposable.addAll(
                    channel.ofNavigation()
                            .subscribe {
                                when(it) {
                                    is MovieNavigation.ToLikeActivity -> {
                                        startActivity(Intent(this@MovieActivity, LikeActivity::class.java))
                                    }

                                    is MovieNavigation.ToWebViewActivity -> {
                                        val intent = Intent(this@MovieActivity, WebViewActivity::class.java).apply {
                                            putExtra(Constants.URL, it.movieLink)
                                        }

                                        startActivity(intent)
                                    }
                                }
                            }
            )
        }
    }

    private fun bindMovieList(looknFeel: MovieLooknFeel.BindMovieRecyclerView) {
        adapter.setMovieLooknFeelList(looknFeel.movieLooknFeelList)
    }

    private fun bindMoreMovieList(looknFeel: MovieLooknFeel.BindMoreMovieRecyclerView) {
        adapter.addMovieLooknFeelList(looknFeel.movieLooknFeelList)
    }

    private fun updateMovieItem(looknFeel: MovieLooknFeel.BindUpdatedMovieItem) {
        adapter.updateItem(looknFeel.movieItem)
    }

    private fun removeMovieItem(looknFeel: MovieLooknFeel.BindRemovedMovieItem) {
        adapter.removeItem(looknFeel.movieItem)
    }

    private fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(baseContext)

        rv_movie_list.apply {
            layoutManager = linearLayoutManager

            adapter = this@MovieActivity.adapter

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(this@MovieActivity, linearLayoutManager.orientation))
        }

        adapter.apply {
            setListener(object : MovieAdapter.AdapterListener {
                override fun onLoadCallback(position: Int) {
                    viewModel.channel.accept(MovieViewAction.LoadMore(start = position))
                }
            })

            setItemClickListener(object : MovieViewHolder.ItemClickListener {
                override fun onClick(item: MovieLayout.LooknFeel) {
                    viewModel.channel.accept(MovieViewAction.Click.ItemElement(item.link))
                }

                override fun onLongClick(item: MovieLayout.LooknFeel) {
                    showDialog(item)
                }
            })
        }
    }

    private fun initializeListener() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        b_search.setOnClickListener {
            imm.hideSoftInputFromWindow(et_movie.windowToken, 0)
            viewModel.channel.accept(MovieViewAction.Click.Search(movieName = et_movie.text.toString()))
        }

        et_movie.setOnEditorActionListener { _, _, _ ->
            imm.hideSoftInputFromWindow(et_movie.windowToken, 0)
            viewModel.channel.accept(MovieViewAction.Click.ActionSearch(movieName = et_movie.text.toString()))

            true
        }
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
                        viewModel.channel.accept(MovieViewAction.Click.RemoveMovie(item))
                    }

                    1 -> {
                        viewModel.channel.accept(MovieViewAction.Click.LikeMovie(item))
                    }
                }
            }
        }.create().show()
    }

}
