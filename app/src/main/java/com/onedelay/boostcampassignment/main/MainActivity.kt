package com.onedelay.boostcampassignment.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.MovieItem
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import com.onedelay.boostcampassignment.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


internal class MainActivity

    : AppCompatActivity(), MovieAdapter.OnMovieListener, MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    private lateinit var searchResultAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, RetrofitApi)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onLoadMoreMovieList(position: Int) {
        presenter.loadMoreMovies(position)
    }

    override fun onMovieItemClick(item: MovieItem) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }
        startActivity(intent)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMovieList(list: List<MovieItem>) {
        searchResultAdapter.addItems(list)
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun showResult() {
        progressBar.visibility = View.GONE
        tv_content.visibility = View.GONE
    }

    override fun showEmptyResult() {
        progressBar.visibility = View.GONE
        tv_content.visibility = View.VISIBLE
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

        button.setOnClickListener {
            requestButton()
        }

        editText.setOnEditorActionListener { _, _, _ ->
            requestButton()
            true
        }
    }

    private fun requestButton() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)

        val isNetworkAlive = presenter.checkNetworkStatus(Utils.isNetworkConnected(this))

        if(isNetworkAlive) {
            searchResultAdapter.clearItems()
            presenter.requestMovies(editText.text.toString())
        }
    }

}
