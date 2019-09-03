package com.onedelay.boostcampassignment.like

import com.onedelay.boostcampassignment.BaseViewModel
import com.onedelay.boostcampassignment.like.channel.LikeChannelApi
import com.onedelay.boostcampassignment.like.repository.LikeRepositoryApi
import io.reactivex.disposables.Disposable
import javax.inject.Inject


internal class LikeViewModel @Inject constructor(
                val channel: LikeChannelApi,
        private val repository: LikeRepositoryApi

) : BaseViewModel() {

    private val lifecycleInput by lazy(LazyThreadSafetyMode.NONE, this::LifecycleInput)
    private val viewActionInput by lazy(LazyThreadSafetyMode.NONE, this::ViewActionInput)
    private val dataInput by lazy(LazyThreadSafetyMode.NONE, this::DataInput)
    private val looknFeelOutput by lazy(LazyThreadSafetyMode.NONE, this::LooknFeelOutput)
    private val navigationOutput by lazy(LazyThreadSafetyMode.NONE, this::NavigationOutput)

    inner class LifecycleInput

    inner class ViewActionInput

    inner class DataInput

    inner class LooknFeelOutput

    inner class NavigationOutput

    init {
        repository.setViewModel(this)

        disposable.addAll(
                *subscribeLooknFeel(),
                *subscribeNavigation()
        )
    }

    private fun subscribeLooknFeel(): Array<Disposable> {
        return looknFeelOutput.run {
            arrayOf(

            )
        }
    }

    private fun subscribeNavigation(): Array<Disposable> {
        return navigationOutput.run {
            arrayOf(

            )
        }
    }

}