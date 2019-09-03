package com.onedelay.boostcampassignment.di

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass


@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)