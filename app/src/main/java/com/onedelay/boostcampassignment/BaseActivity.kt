package com.onedelay.boostcampassignment

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


internal abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelProviderFactory: ViewModelProviderFactory

    protected val activityScopeCompositeDisposable = CompositeDisposable()

    protected fun <VM : ViewModel> createViewModel(clazz: Class<VM>): VM {
        return ViewModelProviders.of(this, viewModelProviderFactory).get(clazz)
    }

}