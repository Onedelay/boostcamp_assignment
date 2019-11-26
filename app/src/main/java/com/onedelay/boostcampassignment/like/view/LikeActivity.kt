package com.onedelay.boostcampassignment.like.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.like.caseProvider.present.LikePresentCaseApi
import com.onedelay.boostcampassignment.like.caseProvider.source.LikeSourceCaseApi
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.dto.LikeNavigation
import com.onedelay.boostcampassignment.like.dto.LikeViewAction
import com.onedelay.boostcampassignment.like.view.custom.EntireLikeLayout
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_like.*
import javax.inject.Inject


internal class LikeActivity : DaggerAppCompatActivity(), LikeView {

    @Inject lateinit var presentCase: LikePresentCaseApi

    @Inject lateinit var sourceCase: LikeSourceCaseApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        initializeListener()

        presentCase.onLifecycle(ActivityLifeCycleState.OnCreate(savedInstanceState))
    }

    override fun onDestroy() {
        presentCase.onLifecycle(ActivityLifeCycleState.OnDestroy())

        super.onDestroy()
    }

    override fun bindLooknFeel(looknFeel: LikeLooknFeel) {
        when (looknFeel) {
            is LikeLooknFeel.BindMovieRecyclerView -> bindMovieList(looknFeel)

            is LikeLooknFeel.BindLikedMovieList -> updateLikedMovieList(looknFeel)
        }
    }

    override fun bindNavigation(navigation: LikeNavigation) {
        when (navigation) {
            is LikeNavigation.ToWebViewActivity -> {
                val intent = Intent(this@LikeActivity, WebViewActivity::class.java).apply {
                    putExtra(Constants.URL, navigation.movieLink)
                }

                startActivity(intent)
            }
        }
    }

    private fun bindMovieList(looknFeel: LikeLooknFeel.BindMovieRecyclerView) {
        custom_fl_entire_like.setLooknFeel(EntireLikeLayout.LooknFeel(looknFeel.movieLooknFeelList.toMutableList()))
    }

    private fun updateLikedMovieList(looknFeel: LikeLooknFeel.BindLikedMovieList) {
        custom_fl_entire_like.updateLikedMovieList(looknFeel)
    }

    private fun initializeListener() {
        custom_fl_entire_like.setListener(object : EntireLikeLayout.Listener {
            override fun onMovieClicked(looknFeel: MovieLayout.LooknFeel) {
                presentCase.onViewAction(LikeViewAction.Click.ItemElement(looknFeel.link))
            }

            override fun onLongClick(looknFeel: MovieLayout.LooknFeel) {
                showDialog(looknFeel)
            }
        })
    }

    private fun showDialog(item: MovieLayout.LooknFeel) {
        val dialogMessages = arrayOf("즐겨찾기 삭제")

        AlertDialog.Builder(this).apply {
            setItems(
                    dialogMessages
            ) { _, which ->
                when (which) {
                    0 -> {
                        presentCase.onViewAction(LikeViewAction.Click.RemoveMovie(item))
                    }
                }
            }
        }.create().show()
    }

}
