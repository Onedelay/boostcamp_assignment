package com.onedelay.boostcampassignment.movie

import android.util.Log
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.BaseViewModel
import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.movie.channel.MovieChannelApi
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import com.onedelay.boostcampassignment.movie.repository.MovieRepositoryApi
import io.reactivex.disposables.Disposable
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class MovieViewModel @Inject constructor(
                val channel: MovieChannelApi,
        private val repository: MovieRepositoryApi

) : BaseViewModel() {

    private val lifecycleInput by lazy(LazyThreadSafetyMode.NONE) { LifecycleInput() }

    val viewActionInput by lazy(LazyThreadSafetyMode.NONE) { ViewActionInput() }

    private val dataInput by lazy(LazyThreadSafetyMode.NONE) { DataInput() }

    private val looknFeelOutput by lazy(LazyThreadSafetyMode.NONE) { LooknFeelOutput() }

    private val navigationOutput by lazy(LazyThreadSafetyMode.NONE) { NavigationOutput() }

    inner class LifecycleInput {
        private val onCreate = channel.ofLifecycle().ofType(ActivityLifeCycleState.OnCreate::class.java)
    }

    inner class ViewActionInput {
        val clickSearch = channel.ofViewAction().ofType(MovieViewAction.Click.Search::class.java)

        val clickActionSearch = channel.ofViewAction().ofType(MovieViewAction.Click.ActionSearch::class.java)

        val clickOptionMenuLike = channel.ofViewAction().ofType(MovieViewAction.Click.OptionMenuLike::class.java)

        val loadMoreMovieList = channel.ofViewAction().ofType(MovieViewAction.LoadMore::class.java)

        val clickMovieItemElement = channel.ofViewAction().ofType(MovieViewAction.Click.ItemElement::class.java)

        val clickMovieLike = channel.ofViewAction().ofType(MovieViewAction.Click.LikeMovie::class.java)

        val clickMovieRemove = channel.ofViewAction().ofType(MovieViewAction.Click.RemoveMovie::class.java)
    }

    inner class DataInput {
        val movieListFetched = channel.ofData().ofType(MovieDataEvent.MovieListFetched::class.java)

        val moreMovieListFetched = channel.ofData().ofType(MovieDataEvent.MoreMovieListFetched::class.java)

        val movieItemRemoved = channel.ofData().ofType(MovieDataEvent.MovieItemRemoved::class.java)

        val movieItemListUpdated = channel.ofData().ofType(MovieDataEvent.LikedMovieItemList::class.java)
    }

    inner class LooknFeelOutput {
        val bindMovieList = dataInput.movieListFetched

        val bindMoreMovieList = dataInput.moreMovieListFetched

        val bindRemovedMovieItem = dataInput.movieItemRemoved

        val bindUpdateMovieItemList = dataInput.movieItemListUpdated
    }

    inner class NavigationOutput {
        val navigateToLikeActivity = viewActionInput.clickOptionMenuLike

        val navigateToWebViewActivity = viewActionInput.clickMovieItemElement
    }

    init {
        repository.setViewModel(this)

        disposable.addAll(
                *subscribeLooknFeel(),
                *subscribeNavigation()
        )
    }

    private fun subscribeLooknFeel(): Array<Disposable> {
        return looknFeelOutput.run {
            arrayOf(
                    bindMovieList
                            .doOnError { Log.d("MY_LOG", "${it.printStackTrace()}") }
                            .subscribe { channel.accept(MovieLooknFeel.BindMovieRecyclerView(transform(it.movieList))) },

                    bindMoreMovieList
                            .doOnError { Log.d("MY_LOG", "${it.printStackTrace()}") }
                            .subscribe { channel.accept(MovieLooknFeel.BindMoreMovieRecyclerView(transform(it.movieList))) },

                    bindRemovedMovieItem
                            .subscribe { channel.accept(MovieLooknFeel.BindRemovedMovieItem(it.movieItem.toLooknFeel())) },

                    bindUpdateMovieItemList
                            .subscribe { channel.accept(MovieLooknFeel.BindLikedMovieList(transform(it.likedMovieList))) }

            )
        }
    }

    private fun subscribeNavigation(): Array<Disposable> {
        return navigationOutput.run {
            arrayOf(
                    navigateToLikeActivity
                            .subscribe { channel.accept(MovieNavigation.ToLikeActivity()) },
                    navigateToWebViewActivity
                            .subscribe { channel.accept(MovieNavigation.ToWebViewActivity(movieLink = it.movieLink)) }
            )
        }
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