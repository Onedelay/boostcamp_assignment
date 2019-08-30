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

    /** View 쪽에서 이것을 observe 하고있다면 뷰한테 변경을 notify 할 필요 없음 */
    private val movieList = ArrayList<MovieItemLookFeel>()

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
                this.movieList.clear()
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

    override fun updateLikedMovie(item: MovieItemLookFeel) {
        if(!item.starred) {
            if(inMemoryDataHolder.addLikedMovie(item)) {
                view.showToastMessage("즐겨찾기 목록에 추가되었습니다.")
            } else {
                view.showToastMessage("오류가 발생했습니다.")
            }
        } else {
            inMemoryDataHolder.removeLikedMovie(item)
            view.showToastMessage("즐겨찾기 목록에서 삭제되었습니다.")
        }

        val position = movieList.indexOf(item)
        movieList[position].starred = !movieList[position].starred

        view.notifyUpdateListItem(movieList[position])
    }

    override fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int) {
        when(which) {
            0 -> view.removeMovieItem(item)

            1 -> updateLikedMovie(item)
        }
    }

    override fun notifyChangedLikedMovieList() {
        updateLikedMovie(this.movieList)

        view.notifyUpdateList(this.movieList)
    }

    private fun requestMovies(position: Int) {
        if(position == 1) {
            view.showProgressBar()
        }

        disposable.addAll(
                movieRepository.fetchMovieList(query = previousQuery, start = position)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { movieList ->
                                    if(movieList.items.isNotEmpty()) {
                                        totalCount = movieList.total

                                        val convertedList = updateLikedMovie(movieList.convertToLookFeel())

                                        this.movieList.addAll(convertedList)

                                        view.run {
                                            showResult()
                                            showMovieList(convertedList)
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
        )
    }

    private fun updateLikedMovie(list: List<MovieItemLookFeel>): List<MovieItemLookFeel> {
        return list.map {
            it.starred = checkIsLiked(it)
            it
        }
    }

    private fun checkIsLiked(movie: MovieItemLookFeel) = inMemoryDataHolder.getLikedMovieMap().containsKey(movie.link)

}