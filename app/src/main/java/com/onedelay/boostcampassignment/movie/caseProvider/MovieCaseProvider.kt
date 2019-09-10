package com.onedelay.boostcampassignment.movie.caseProvider

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.movie.MovieScreen
import com.onedelay.boostcampassignment.movie.channel.MovieChannelApi
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


internal class MovieCaseProvider @Inject constructor(
                     private val channel: MovieChannelApi,
        @MovieScreen private val compositeDisposable: CompositeDisposable

) : MovieCaseProviderApi {

    private val lifecycleInput by lazy(LazyThreadSafetyMode.NONE) { LifecycleInput() }

    private val viewActionInput by lazy(LazyThreadSafetyMode.NONE) { ViewActionInput() }

    private val dataInput by lazy(LazyThreadSafetyMode.NONE) { DataInput() }

    inner class LifecycleInput {
        val onCreate = channel.ofLifecycle().ofType(ActivityLifeCycleState.OnCreate::class.java)

        val onDestory = channel.ofLifecycle().ofType(ActivityLifeCycleState.OnDestroy::class.java)
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

    init {
        compositeDisposable.addAll(
                lifecycleInput.onDestory
                        .subscribe { compositeDisposable.clear() }
        )
    }

    override fun channel() = channel

    override fun disposable() = compositeDisposable

    override fun lifecycle() = lifecycleInput

    override fun viewAction() = viewActionInput

    override fun data() = dataInput

}