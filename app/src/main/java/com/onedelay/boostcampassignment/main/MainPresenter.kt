package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.MovieListRepository
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


internal class MainPresenter constructor(
        private val view: MainContract.View,
        private val movieRepository: MovieListRepository,
        private val inMemoryDataHolder: InMemoryDataHolder

) : MainContract.Presenter {

    private val disposable = CompositeDisposable()

    private var previousQuery = ""
    private var totalCount    = 0

    override fun onDestroy() {
        disposable.clear()
    }

    override fun checkNetworkStatus(status: Boolean): Boolean {
        if(!status) {
            view.run {
                showEmptyResult()
                showToastMessage("인터넷 연결을 확인해주세요")
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
                showToastMessage("검색어를 입력해주세요")
            }
        }
    }

    override fun loadMoreMovies(position: Int) {
        if(totalCount > position) {
            requestMovies(position)
        }
    }

    override fun addLikedMovie(item: MovieItemLookFeel) {
        if(inMemoryDataHolder.addLikedMovie(item)) {
            view.showToastMessage("즐겨찾기 목록에 추가되었습니다.")
        } else {
            view.showToastMessage("오류가 발생했습니다.")
        }
    }

    override fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int) {
        when(which) {
            0 -> {
                view.removeMovieItem(item)
            }

            1 -> {
                addLikedMovie(item)
            }
        }
    }

    private fun requestMovies(position: Int) {
        if(position == 1) {
            view.showProgressBar()
        }

        movieRepository.fetchMovieList(query = previousQuery, start = position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if(it.items.isNotEmpty()) {
                                totalCount = it.total
                                view.run {
                                    showResult()
                                    showMovieList(it.convertToLookFeel())
                                }
                            } else {
                                view.showEmptyResult()
                            }
                        },
                        {
                            it.printStackTrace()
                            view.run {
                                showEmptyResult()
                                showToastMessage("예상치 못한 오류가 발생했습니다.")
                            }
                        })
                .also {
                    disposable.add(it)
                }
    }

}