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
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class MainActivity : AppCompatActivity(), MovieAdapter.OnMovieListener {
    private var adapter: MovieAdapter? = null
    private var count = 0
    private var total = 0
    private var search = "" // 이전 검색어

    private var subscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        button.setOnClickListener {
            requestButton()
        }
    }

    private fun requestButton() {
        // 이미 검색한 검색어는 검색 안함
        if (search != editText.text.toString()) {
            search = editText.text.toString()
            if (Utils.isNetworkConnected(this)) {
                if (search.isNotEmpty()) {
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
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this))
    }

    override fun onLoadMoreMovieList(position: Int) {
        // 마지막에 받은 데이터가 10개 이상이고
        // start 요청쿼리(position)가 total 을 넘어설 경우 서버에서 짤라서 보내주는것 방지
        if (total > position) requestMovies(position)
    }

    override fun onMovieItemClick(item: MovieItem) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        intent.putExtra(Constants.URL, item.link)
        startActivity(intent)
    }

    private fun requestMovies(position: Int) {
        if (position == 1) progressBar.visibility = View.VISIBLE

        RetrofitApi.service.requestMovieInfo(search, position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            adapter!!.addItems(it.items)
                            count = it.items.size
                            total = it.total
                            progressBar.visibility = View.GONE

                            tv_content.visibility = if (count == 0) View.VISIBLE else View.GONE
                        },
                        {
                            it.printStackTrace()
                            progressBar.visibility = View.GONE
                        }
                ).apply { subscription.add(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.clear()
    }
}
