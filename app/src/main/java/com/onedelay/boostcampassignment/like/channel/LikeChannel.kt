package com.onedelay.boostcampassignment.like.channel

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.dto.LikeNavigation
import com.onedelay.boostcampassignment.like.dto.LikeViewAction
import io.reactivex.Observable
import javax.inject.Inject


internal class LikeChannel @Inject constructor() : LikeChannelApi {

    private val lifecycleChannel: Relay<ActivityLifeCycleState> = PublishRelay.create()

    private val looknFeelChannel: Relay<LikeLooknFeel> = PublishRelay.create()

    private val viewActionChannel: Relay<LikeViewAction> = PublishRelay.create()

    private val navigationChannel: Relay<LikeNavigation> = PublishRelay.create()

    private val dataChannel: Relay<LikeDataEvent> = PublishRelay.create()

    override fun ofLifecycle(): Observable<ActivityLifeCycleState> = lifecycleChannel

    override fun ofLooknFeel(): Observable<LikeLooknFeel> = looknFeelChannel

    override fun ofViewAction(): Observable<LikeViewAction> = viewActionChannel

    override fun ofNavigation(): Observable<LikeNavigation> = navigationChannel

    override fun ofData(): Observable<LikeDataEvent> = dataChannel

    override fun accept(lifecycle: ActivityLifeCycleState) = lifecycleChannel.accept(lifecycle)

    override fun accept(looknFeel: LikeLooknFeel) = looknFeelChannel.accept(looknFeel)

    override fun accept(viewAction: LikeViewAction) = viewActionChannel.accept(viewAction)

    override fun accept(navigation: LikeNavigation) = navigationChannel.accept(navigation)

    override fun accept(data: LikeDataEvent) = dataChannel.accept(data)

}