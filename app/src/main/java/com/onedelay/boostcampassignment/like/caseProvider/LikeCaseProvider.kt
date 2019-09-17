package com.onedelay.boostcampassignment.like.caseProvider

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.like.LikeScreen
import com.onedelay.boostcampassignment.like.channel.LikeChannelApi
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.dto.LikeViewAction
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class LikeCaseProvider @Inject constructor(
                    private val channel: LikeChannelApi,
        @LikeScreen private val compositeDisposable: CompositeDisposable

) : LikeCaseProviderApi {

    private val lifeCycleInput by lazy(LazyThreadSafetyMode.NONE) { LifeCycleInput() }

    private val viewActionInput by lazy(LazyThreadSafetyMode.NONE) { ViewActionInput() }

    private val dataInput by lazy(LazyThreadSafetyMode.NONE) { DataInput() }

    inner class LifeCycleInput {
        val onCreate = channel.ofLifecycle().ofType(ActivityLifeCycleState.OnCreate::class.java)

        val onDestroy = channel.ofLifecycle().ofType(ActivityLifeCycleState.OnDestroy::class.java)
    }

    inner class ViewActionInput {
        val clickMovieItemElement = channel.ofViewAction().ofType(LikeViewAction.Click.ItemElement::class.java)

        val clickMovieLike = channel.ofViewAction().ofType(LikeViewAction.Click.RemoveMovie::class.java)
    }

    inner class DataInput {
        val likeMovieListFetched = channel.ofData().ofType(LikeDataEvent.LikedMovieListFetched::class.java)

        val likeMovieListUpdated = channel.ofData().ofType(LikeDataEvent.LikedMovieItemList::class.java)
    }

    init {
        compositeDisposable.addAll(
                lifeCycleInput.onDestroy.subscribe { compositeDisposable.clear() }
        )
    }

    override fun channel() = channel

    override fun disposable() = compositeDisposable

    override fun lifecycle() = lifeCycleInput

    override fun viewAction() = viewActionInput

    override fun data() = dataInput

}