package com.onedelay.boostcampassignment.like.caseProvider.present

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.like.dto.LikeViewAction


internal interface LikePresentCaseApi {
    fun onLifecycle(lifecycle: ActivityLifeCycleState)
    fun onViewAction(viewAction: LikeViewAction)
}