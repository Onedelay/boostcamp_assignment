package com.onedelay.boostcampassignment

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


internal open class BaseViewModel : ViewModel() {

    val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}