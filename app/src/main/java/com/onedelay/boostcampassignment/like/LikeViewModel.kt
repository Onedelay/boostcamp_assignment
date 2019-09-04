package com.onedelay.boostcampassignment.like

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.BaseViewModel
import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.like.channel.LikeChannelApi
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.repository.LikeRepositoryApi
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import io.reactivex.disposables.Disposable
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class LikeViewModel @Inject constructor(
                val channel: LikeChannelApi,
        private val repository: LikeRepositoryApi

) : BaseViewModel() {

    val lifecycleInput by lazy(LazyThreadSafetyMode.NONE, this::LifecycleInput)
    private val viewActionInput by lazy(LazyThreadSafetyMode.NONE, this::ViewActionInput)
    private val dataInput by lazy(LazyThreadSafetyMode.NONE, this::DataInput)
    private val looknFeelOutput by lazy(LazyThreadSafetyMode.NONE, this::LooknFeelOutput)
    private val navigationOutput by lazy(LazyThreadSafetyMode.NONE, this::NavigationOutput)

    inner class LifecycleInput {
        val onCreate = channel.ofLifecycle().ofType(ActivityLifeCycleState.OnCreate::class.java)
    }

    inner class ViewActionInput

    inner class DataInput {
        val likedMovieListFetched = channel.ofData().ofType(LikeDataEvent.LikedMovieListFetched::class.java)
    }

    inner class LooknFeelOutput {
        val bindMovieList = dataInput.likedMovieListFetched
    }

    inner class NavigationOutput

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
                        .subscribe {
                            val list = transform(it.likedMovieList).map { movie ->
                                movie.starred = true
                                movie
                            }
                            channel.accept(LikeLooknFeel.BindMovieRecyclerView(list))
                        }
            )
        }
    }

    private fun subscribeNavigation(): Array<Disposable> {
        return navigationOutput.run {
            arrayOf(

            )
        }
    }

    private fun transform(movieList: List<Movie>): List<MovieLayout.LooknFeel> {
        return movieList.map {
            MovieLayout.LooknFeel(
                    title      = it.title,
                    link       = it.link,
                    image      = it.image,
                    pubDate    = it.pubDate,
                    director   = it.director,
                    actor      = it.actor,
                    userRating = it.userRating
            )
        }
    }

}