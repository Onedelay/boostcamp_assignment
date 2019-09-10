package com.onedelay.boostcampassignment.movie.caseProvider.present

import com.onedelay.boostcampassignment.ActivityLifeCycleState
import com.onedelay.boostcampassignment.movie.dto.MovieViewAction


internal interface MoviePresentCaseApi {
    fun onLifecycle(lifecycle: ActivityLifeCycleState)
    fun onViewAction(viewAction: MovieViewAction)
}