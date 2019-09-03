package com.onedelay.boostcampassignment.like.repository

import com.onedelay.boostcampassignment.like.LikeViewModel
import com.onedelay.boostcampassignment.like.source.LikeDataSourceApi
import javax.inject.Inject


internal class LikeRepository @Inject constructor(
        private val dataSource: LikeDataSourceApi

) : LikeRepositoryApi {

    override fun setViewModel(viewModel: LikeViewModel) {
        viewModel.run {

            disposable.addAll(

            )
        }
    }

}