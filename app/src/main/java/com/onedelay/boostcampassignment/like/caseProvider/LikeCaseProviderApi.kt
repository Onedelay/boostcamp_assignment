package com.onedelay.boostcampassignment.like.caseProvider

import com.onedelay.boostcampassignment.like.channel.LikeChannelApi
import io.reactivex.disposables.CompositeDisposable


internal interface LikeCaseProviderApi {
    fun channel(): LikeChannelApi
    fun disposable(): CompositeDisposable
    fun lifecycle(): LikeCaseProvider.LifeCycleInput
    fun viewAction(): LikeCaseProvider.ViewActionInput
    fun data(): LikeCaseProvider.DataInput
}