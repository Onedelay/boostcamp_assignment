package com.onedelay.boostcampassignment.movie.channel

import com.jakewharton.rxrelay2.PublishRelay
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieChannel @Inject constructor() : MovieChannelApi {

    private val lifecycleChannel = PublishRelay.create<ActivityLifeCycleState>()

    private val viewActionChannel = PublishRelay.create<MovieViewAction>()

    private val dataChannel = PublishRelay.create<MovieDataEvent>()

    private val looknFeelChannel = PublishRelay.create<MovieLooknFeel>()

    private val navigationChannel = PublishRelay.create<MovieNavigation>()

    override fun ofLifecycle(): Observable<ActivityLifeCycleState> = lifecycleChannel

    override fun ofViewAction(): Observable<MovieViewAction> = viewActionChannel

    override fun ofData(): Observable<MovieDataEvent> = dataChannel

    override fun ofLooknFeel(): Observable<MovieLooknFeel> = looknFeelChannel

    override fun ofNavigation(): Observable<MovieNavigation> = navigationChannel

    override fun accept(lifecycle: ActivityLifeCycleState) = lifecycleChannel.accept(lifecycle)

    override fun accept(viewAction: MovieViewAction) = viewActionChannel.accept(viewAction)

    override fun accept(dataEvent: MovieDataEvent) = dataChannel.accept(dataEvent)

    override fun accept(looknFeel: MovieLooknFeel) = looknFeelChannel.accept(looknFeel)

    override fun accept(navigation: MovieNavigation) = navigationChannel.accept(navigation)

}