package com.onedelay.boostcampassignment.movie.caseProvider.present

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.movie.caseProvider.MovieCaseProvider
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import com.onedelay.boostcampassignment.movie.view.MovieView
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class MoviePresenterCase @Inject constructor(
        private val weakView: WeakReference<MovieView>,
        private val caseProvider: MovieCaseProvider

) : MoviePresentCaseApi {

    private val looknFeelOutput by lazy(LazyThreadSafetyMode.NONE) { LooknFeelOutput() }

    private val navigationOutput by lazy(LazyThreadSafetyMode.NONE) { NavigationOutput() }

    inner class LooknFeelOutput {
        val bindMovieList = caseProvider.DataInput().movieListFetched

        val bindMoreMovieList = caseProvider.DataInput().moreMovieListFetched

        val bindRemovedMovieItem = caseProvider.DataInput().movieItemRemoved

        val bindUpdateMovieItemList = caseProvider.DataInput().movieItemListUpdated
    }

    inner class NavigationOutput {
        val navigateToLikeActivity = caseProvider.ViewActionInput().clickOptionMenuLike

        val navigateToWebViewActivity = caseProvider.ViewActionInput().clickMovieItemElement
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
                    bindMovieList
                            .subscribe { weakView.get()?.bindLooknFeel(MovieLooknFeel.BindMovieRecyclerView(transform(it.movieList))) },

                    bindMoreMovieList
                            .subscribe { weakView.get()?.bindLooknFeel(MovieLooknFeel.BindMoreMovieRecyclerView(transform(it.movieList))) },

                    bindRemovedMovieItem
                            .subscribe { weakView.get()?.bindLooknFeel(MovieLooknFeel.BindRemovedMovieItem(it.movieItem.toLooknFeel())) },

                    bindUpdateMovieItemList
                            .subscribe { weakView.get()?.bindLooknFeel(MovieLooknFeel.BindLikedMovieList(transform(it.likedMovieList))) }
            )
        }
    }

    private fun subscribeNavigation(): Array<Disposable> {
        return navigationOutput.run {
            arrayOf(
                    navigateToLikeActivity
                            .subscribe { weakView.get()?.bindNavigation(MovieNavigation.ToLikeActivity()) },
                    navigateToWebViewActivity
                            .subscribe { weakView.get()?.bindNavigation(MovieNavigation.ToWebViewActivity(movieLink = it.movieLink)) }
            )
        }
    }

    override fun onLifecycle(lifecycle: ActivityLifeCycleState) {
        caseProvider.channel().accept(lifecycle)
    }

    override fun onViewAction(viewAction: MovieViewAction) {
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