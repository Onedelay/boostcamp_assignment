package com.onedelay.boostcampassignment.like

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.BaseActivity
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.dto.LikeNavigation
import com.onedelay.boostcampassignment.like.dto.LikeViewAction
import com.onedelay.boostcampassignment.movie.MovieAdapter
import com.onedelay.boostcampassignment.movie.MovieViewHolder
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import kotlinx.android.synthetic.main.activity_like.*
import javax.inject.Inject


internal class LikeActivity : BaseActivity() {

    private val viewModel by lazy { createViewModel(LikeViewModel::class.java) }

    @Inject lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        subscribeLooknFeel()
        subscribeNavigation()

        initializeRecyclerView()

        initializeListener()

        viewModel.channel.accept(ActivityLifeCycleState.OnCreate())
    }

    private fun subscribeLooknFeel() {
        activityScopeCompositeDisposable.addAll(
            viewModel.channel.ofLooknFeel()
                    .subscribe {
                        when (it) {
                            is LikeLooknFeel.BindMovieRecyclerView -> {
                                adapter.setMovieLooknFeelList(list = it.movieLooknFeelList)
                            }

                            is LikeLooknFeel.BindLikedMovieList -> {
                                updateLikedMovieList(it)
                            }
                        }
                    }
        )
    }

    private fun subscribeNavigation() {
        activityScopeCompositeDisposable.addAll(
                viewModel.channel.ofNavigation().ofType(LikeNavigation::class.java)
                        .subscribe {
                            when (it) {
                                is LikeNavigation.ToWebViewActivity -> {
                                    val intent = Intent(this@LikeActivity, WebViewActivity::class.java).apply {
                                        putExtra(Constants.URL, it.movieLink)
                                    }

                                    startActivity(intent)
                                }
                            }
                        }
        )
    }

    private fun updateLikedMovieList(looknFeel: LikeLooknFeel.BindLikedMovieList) {
        looknFeel.likedMovieLooknFeelList.forEach {
            adapter.updateItem(it)
        }
    }

    private fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(baseContext)

        rv_movie_list.apply {
            layoutManager = linearLayoutManager

            adapter = this@LikeActivity.adapter

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(this@LikeActivity, linearLayoutManager.orientation))
        }

        adapter.apply {
            setListener(object : MovieAdapter.AdapterListener {
                override fun onLoadCallback(position: Int) {
                    // do nothing
                }
            })

            setItemClickListener(object : MovieViewHolder.ItemClickListener {
                override fun onClick(item: MovieLayout.LooknFeel) {
                    viewModel.channel.accept(LikeViewAction.Click.ItemElement(item.link))
                }

                override fun onLongClick(item: MovieLayout.LooknFeel) {
                    showDialog(item)
                }
            })
        }
    }

    private fun initializeListener() {

    }

    private fun showDialog(item: MovieLayout.LooknFeel) {
        val dialogMessages = arrayOf("즐겨찾기 삭제")

        AlertDialog.Builder(this).apply {
            setItems(
                    dialogMessages
            ) { _, which ->
                when (which) {
                    0 -> {
                        viewModel.channel.accept(LikeViewAction.Click.RemoveMovie(item))
                    }
                }
            }
        }.create().show()
    }

}
