package com.onedelay.boostcampassignment.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.model.MovieItem
import com.onedelay.boostcampassignment.model.RetrofitApi
import com.onedelay.boostcampassignment.utils.Constants
import com.onedelay.boostcampassignment.utils.DividerItemDecoration
import com.onedelay.boostcampassignment.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity(), MovieAdapter.OnMovieListener {
    private var adapter: MovieAdapter? = null
    private var count = 0
    private var total = 0
    private var search = "" // 이전 검색어

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        button.setOnClickListener {
            requestButton()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun requestButton() {
        if(search != editText.text.toString()) {
            search = editText.text.toString()
            if(Utils.isNetworkConnected(this)) {
                if(search.isNotEmpty()) {
                    editText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                    adapter?.clearItems()
                    requestMovies(1)
                } else {
                    Toast.makeText(this, resources.getText(R.string.toast_msg_edittext_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, resources.getText(R.string.toast_msg_network_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        adapter = MovieAdapter(this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(baseContext)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
            addItemDecoration(DividerItemDecoration(this@MainActivity))
        }
    }

    override fun onLoadMoreMovieList(position: Int) {
        if(total > position) {
            requestMovies(position)
        }
    }

    override fun onMovieItemClick(item: MovieItem) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }
        startActivity(intent)
    }

    private fun requestMovies(position: Int) {
        if(position == 1) {
            progressBar.visibility = View.VISIBLE
        }

        disposable.add(
                RetrofitApi.service.requestMovieInfo(search, position)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                adapter?.addItems(it.items)
                                count = it.items.size
                                total = it.total
                                progressBar.visibility = View.GONE

                                tv_content.visibility = if (count == 0) View.VISIBLE else View.GONE
                            },
                            {
                                it.printStackTrace()
                                progressBar.visibility = View.GONE
                            }
                ))
    }

}
