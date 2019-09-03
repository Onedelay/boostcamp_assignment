package com.onedelay.boostcampassignment.movie.channel

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction
import io.reactivex.Observable


internal interface MovieChannelApi {
    fun ofLifecycle(): Observable<ActivityLifeCycleState>
    fun ofViewAction(): Observable<MovieViewAction>
    fun ofData(): Observable<MovieDataEvent>
    fun ofNavigation(): Observable<MovieNavigation>
    fun ofLooknFeel(): Observable<MovieLooknFeel>
    fun accept(lifecycle: ActivityLifeCycleState)
    fun accept(viewAction: MovieViewAction)
    fun accept(dataEvent: MovieDataEvent)
    fun accept(looknFeel: MovieLooknFeel)
    fun accept(navigation: MovieNavigation)
}