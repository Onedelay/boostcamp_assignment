package com.onedelay.boostcampassignment


internal sealed class ActivityLifeCycleState {
    class OnCreate : ActivityLifeCycleState()

    class OnDestroy : ActivityLifeCycleState()
}