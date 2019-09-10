package com.onedelay.boostcampassignment

import android.os.Bundle


internal sealed class ActivityLifeCycleState {
    class OnCreate(val savedInstanceState: Bundle?) : ActivityLifeCycleState()

    class OnDestroy : ActivityLifeCycleState()
}