package com.onedelay.boostcampassignment.like.caseProvider.present

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.like.caseProvider.LikeCaseProviderApi
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.dto.LikeNavigation
import com.onedelay.boostcampassignment.like.dto.LikeViewAction
import com.onedelay.boostcampassignment.like.view.LikeView
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class LikePresentCase @Inject constructor(
        private val weakView: WeakReference<LikeView>,
        private val caseProvider: LikeCaseProviderApi

) : LikePresentCaseApi {

    private val looknFeelOutput by lazy(LazyThreadSafetyMode.NONE) { LooknFeelOutput() }

    private val navigationOutput by lazy(LazyThreadSafetyMode.NONE) { NavigationOutput() }

    inner class LooknFeelOutput {
        val bindLikedMovieList = caseProvider.data().likeMovieListFetched

        val bindUpdateMovieItemList = caseProvider.data().likeMovieListUpdated
    }

    inner class NavigationOutput {
        val navigateToWebViewActivity = caseProvider.viewAction().clickMovieItemElement
    }

    init {
        caseProvider.disposable().addAll(
                *subscribeLooknFeel(),
                *subscribeNavigation()
        )
    }

    private fun subscribeLooknFeel(): Array<Disposable> {
        return looknFeelOutput.run {
            arrayOf(
                    bindLikedMovieList
                            .subscribe { weakView.get()?.bindLooknFeel(LikeLooknFeel.BindMovieRecyclerView(transform(it.likedMovieList))) },
                    bindUpdateMovieItemList
                            .subscribe { weakView.get()?.bindLooknFeel(LikeLooknFeel.BindLikedMovieList(transform(it.likedMovieList))) }
            )
        }
    }

    private fun subscribeNavigation(): Array<Disposable> {
        return navigationOutput.run {
            arrayOf(
                    navigateToWebViewActivity
                            .subscribe { weakView.get()?.bindNavigation(LikeNavigation.ToWebViewActivity(it.movieLink)) }
            )
        }
    }

    override fun onLifecycle(lifecycle: ActivityLifeCycleState) {
        caseProvider.channel().accept(lifecycle)
    }

    override fun onViewAction(viewAction: LikeViewAction) {
        caseProvider.channel().accept(viewAction)
    }

    private fun transform(movieList: List<Movie>): List<MovieLayout.LooknFeel> {
        return movieList.map { it.toLooknFeel() }
    }

    private fun Movie.toLooknFeel(): MovieLayout.LooknFeel {
        val isChecked = starred
        return MovieLayout.LooknFeel(
                title      = title,
                link       = link,
                image      = image,
                pubDate    = pubDate,
                director   = director,
                actor      = actor,
                userRating = userRating
        ).apply {
            starred = isChecked
        }
    }

}