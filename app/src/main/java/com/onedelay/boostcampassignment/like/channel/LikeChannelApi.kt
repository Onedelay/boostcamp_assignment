package com.onedelay.boostcampassignment.like.channel

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.dto.LikeNavigation
import com.onedelay.boostcampassignment.like.dto.LikeViewAction
import io.reactivex.Observable


internal interface LikeChannelApi {
    fun ofLifecycle(): Observable<ActivityLifeCycleState>
    fun ofLooknFeel(): Observable<LikeLooknFeel>
    fun ofViewAction(): Observable<LikeViewAction>
    fun ofNavigation(): Observable<LikeNavigation>
    fun ofData(): Observable<LikeDataEvent>
    fun accept(lifecycle: ActivityLifeCycleState)
    fun accept(looknFeel: LikeLooknFeel)
    fun accept(viewAction: LikeViewAction)
    fun accept(navigation: LikeNavigation)
    fun accept(data: LikeDataEvent)
}
