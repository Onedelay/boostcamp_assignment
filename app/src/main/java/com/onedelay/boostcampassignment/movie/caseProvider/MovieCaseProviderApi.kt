package com.onedelay.boostcampassignment.movie.caseProvider

import com.onedelay.boostcampassignment.movie.channel.MovieChannelApi
import io.reactivex.disposables.CompositeDisposable


internal interface MovieCaseProviderApi {
    fun channel(): MovieChannelApi
    fun disposable(): CompositeDisposable
    fun lifecycle(): MovieCaseProvider.LifecycleInput
    fun viewAction(): MovieCaseProvider.ViewActionInput
    fun data(): MovieCaseProvider.DataInput
}