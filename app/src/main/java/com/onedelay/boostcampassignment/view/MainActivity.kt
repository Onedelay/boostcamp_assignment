package com.onedelay.boostcampassignment.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.model.MovieItem
import com.onedelay.boostcampassignment.model.RetrofitApi
import com.onedelay.boostcampassignment.utils.Constants
import com.onedelay.boostcampassignment.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


internal class MainActivity : AppCompatActivity(), MovieAdapter.OnMovieListener {

    private lateinit var searchResultAdapter: MovieAdapter

    private var loadedCount   = 0
    private var totalCount    = 0
    private var previousQuery = ""

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun onLoadMoreMovieList(position: Int) {
        if(totalCount > position) {
            requestMovies(position)
        }
    }

    override fun onMovieItemClick(item: MovieItem) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }
        startActivity(intent)
    }

    private fun initViews() {
        searchResultAdapter = MovieAdapter(this)

        val linearLayoutManager = LinearLayoutManager(baseContext)

        recyclerView.apply {
            adapter       = this@MainActivity.searchResultAdapter
            layoutManager = linearLayoutManager

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(this@MainActivity, linearLayoutManager.orientation))
        }

        button.setOnClickListener { requestButton() }
    }

    private fun requestButton() {
        if(previousQuery != editText.text.toString()) {
            previousQuery = editText.text.toString()

            if(Utils.isNetworkConnected(this)) {
                if(previousQuery.isNotEmpty()) {
                    editText.onEditorAction(EditorInfo.IME_ACTION_NEXT)

                    searchResultAdapter.clearItems()

                    requestMovies(1)
                } else {
                    Toast.makeText(this, resources.getText(R.string.toast_msg_edittext_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, resources.getText(R.string.toast_msg_network_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestMovies(position: Int) {
        if(position == 1) {
            progressBar.visibility = View.VISIBLE
        }

        disposable.add(RetrofitApi.service.requestMovieInfo(query = previousQuery, start = position)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    searchResultAdapter.addItems(it.items)
                                    loadedCount = it.items.size
                                    totalCount = it.total

                                    progressBar.visibility = View.GONE
                                    tv_content.visibility = if (loadedCount == 0) View.VISIBLE else View.GONE
                                },
                                {
                                    it.printStackTrace()
                                    progressBar.visibility = View.GONE
                                }))
    }

}
