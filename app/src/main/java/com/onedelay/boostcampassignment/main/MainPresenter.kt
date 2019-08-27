package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.source.RetrofitApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


internal class MainPresenter constructor(
        private val view: MainContract.View,
        private val remoteSource: RetrofitApi

) : MainContract.Presenter {

    private val disposable = CompositeDisposable()

    private var previousQuery = ""
    private var totalCount    = 0

    override fun onDestroy() {
        disposable.clear()
    }

    override fun checkNetworkStatus(status: Boolean): Boolean {
        if (!status) {
            view.run {
                showEmptyResult()
                showErrorMessage("인터넷 연결을 확인해주세요")
            }
        }
        return status
    }

    override fun requestMovies(query: String) {
        if(query.isNotEmpty()) {
            if(previousQuery != query) {
                previousQuery = query
                requestMovies(1)
            }
        }  else {
            view.run {
                showEmptyResult()
                showErrorMessage("검색어를 입력해주세요")
            }
        }
    }

    override fun loadMoreMovies(position: Int) {
        if(totalCount > position) {
            requestMovies(position)
        }
    }

    private fun requestMovies(position: Int) {
        if (position == 1) {
            view.showProgressBar()
        }

        remoteSource.service.requestMovieInfo(query = previousQuery, start = position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if(it.items.isNotEmpty()) {
                                totalCount = it.total
                                view.run {
                                    showResult()
                                    showMovieList(it.items)
                                }
                            } else {
                                view.showEmptyResult()
                            }
                        },
                        {
                            it.printStackTrace()
                            view.run {
                                showEmptyResult()
                                showErrorMessage("예상치 못한 오류가 발생했습니다")
                            }
                        })
                .also {
                    disposable.add(it)
                }
    }

}