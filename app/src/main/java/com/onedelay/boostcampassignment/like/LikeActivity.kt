package com.onedelay.boostcampassignment.like

import android.os.Bundle
import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.BaseActivity
import com.onedelay.boostcampassignment.R


internal class LikeActivity : BaseActivity() {

    private val viewModel by lazy { createViewModel(LikeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        subscribeLooknFeel()
        subscribeNavigation()

        initializeListener()

        viewModel.channel.accept(ActivityLifeCycleState.OnCreate())
    }

    private fun subscribeLooknFeel() {
        with(viewModel) {
            activityScopeCompositeDisposable.addAll(

            )
        }
    }

    private fun subscribeNavigation() {
        activityScopeCompositeDisposable.addAll(
                viewModel.channel.ofNavigation()
                        .subscribe {

                        }
        )
    }

    private fun initializeListener() {

    }

}
