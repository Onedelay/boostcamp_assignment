package com.onedelay.boostcampassignment.movie

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.BaseActivity
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.liked.LikedMovieActivity
import com.onedelay.boostcampassignment.main.MainActivity
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
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

        Log.d("MY_LOG", "onCreate")
    }

    override fun onDestroy() {
        Log.d("MY_LOG", "onDestroy")

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
                                    is  MovieLooknFeel.BindMovieRecyclerView -> bindMovieList(it)
                                }
                            }
            )
        }
    }

    private fun subscribeNavigation() {
        with(viewModel) {
            activityScopeCompositeDisposable.addAll(
                    channel.ofNavigation()
                            .doOnError { Log.d("MY_LOG", "${it.printStackTrace()}") }
                            .subscribe {
                                when(it) {
                                    is MovieNavigation.ToLikeActivity -> startActivity(Intent(this@MovieActivity, LikedMovieActivity::class.java))
                                }
                            }
            )
        }
    }

    private fun bindMovieList(looknFeel: MovieLooknFeel.BindMovieRecyclerView) {
        adapter.setMovieLooknFeelList(looknFeel.movieLooknFeelList)
    }

    private fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(baseContext)

        rv_movie_list.apply {
            layoutManager = linearLayoutManager

            adapter = this@MovieActivity.adapter

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(this@MovieActivity, linearLayoutManager.orientation))
        }
    }

    private fun initializeListener() {
        b_search.setOnClickListener {
            viewModel.channel.accept(MovieViewAction.Click.Search(movieName = et_movie.text.toString()))
        }

        et_movie.setOnEditorActionListener { _, _, _ ->
            viewModel.channel.accept(MovieViewAction.Click.ActionSearch(movieName = et_movie.text.toString()))

            true
        }
    }
}
