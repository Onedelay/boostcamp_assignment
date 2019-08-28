package com.onedelay.boostcampassignment.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.MovieItem
import com.onedelay.boostcampassignment.data.MovieListRepository
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import com.onedelay.boostcampassignment.liked.LikedMovieActivity
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import com.onedelay.boostcampassignment.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*


internal class MainActivity

    : AppCompatActivity(), MovieViewHolder.ItemClickListener, MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    private lateinit var searchResultAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, MovieListRepository(RetrofitApi), InMemoryDataHolder)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.menu_liked_movie -> {
                startActivity(Intent(this, LikedMovieActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(item: MovieItem) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }
        startActivity(intent)
    }

    override fun onLongClick(item: MovieItem) {
        val builder = AlertDialog.Builder(this).apply {
            setItems(
                    arrayOf("삭제", "즐겨찾기"),
                    DialogInterface.OnClickListener { _, which ->
                        presenter.selectDialogMenuOf(item, which)
                    })
        }
        builder.create().show()
    }

    override fun showToastMessage(message: String) {
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

    override fun removeMovieItem(item: MovieItem) {
        searchResultAdapter.removeItem(item)
    }

    private fun initViews() {
        initAdapter()

        button.setOnClickListener {
            requestButton()
        }

        editText.setOnEditorActionListener { _, _, _ ->
            requestButton()
            true
        }
    }

    private fun initAdapter() {
        searchResultAdapter = MovieAdapter(this) { position ->
            presenter.loadMoreMovies(position) // FIXME: 가독성이 떨어지는 것 같기도 함, 추후에 어댑터에 인터페이스 추가하게되면 번거로움
        }

        val linearLayoutManager = LinearLayoutManager(baseContext)

        recyclerView.apply {
            adapter       = this@MainActivity.searchResultAdapter
            layoutManager = linearLayoutManager

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(this@MainActivity, linearLayoutManager.orientation))
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
