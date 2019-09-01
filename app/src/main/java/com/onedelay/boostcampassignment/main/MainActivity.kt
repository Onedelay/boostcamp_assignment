package com.onedelay.boostcampassignment.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import com.onedelay.boostcampassignment.liked.LikedMovieActivity
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import com.onedelay.boostcampassignment.utils.Utils
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


internal class MainActivity : DaggerAppCompatActivity(), MovieViewHolder.ItemClickListener, MainContract.View {

    companion object {
        const val REQUEST_CODE = 1111
    }

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private lateinit var searchResultAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode) {
            LikedMovieActivity.RESULT_CODE_DATA_CHANGED -> presenter.notifyChangedLikedMovieList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.menu_liked_movie -> {
                startActivityForResult(Intent(this, LikedMovieActivity::class.java), REQUEST_CODE)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(item: MovieItemLookFeel) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }
        startActivity(intent)
    }

    override fun onLongClick(item: MovieItemLookFeel) {
       showDialog(item)
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMovieList(list: List<MovieItemLookFeel>) {
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

    override fun removeMovieItem(item: MovieItemLookFeel) {
        searchResultAdapter.removeItem(item)
    }

    override fun notifyUpdateListItem(item: MovieItemLookFeel) {
        searchResultAdapter.updateItem(item)
    }

    override fun notifyUpdateList(list: List<MovieItemLookFeel>) {
        searchResultAdapter.run {
            clearItems()
            addItems(list)
            notifyDataSetChanged()
        }
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
            adapter = this@MainActivity.searchResultAdapter
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

    private fun showDialog(item: MovieItemLookFeel) {
        val dialogMessages = if(!item.starred) {
            arrayOf("삭제", "즐겨찾기")
        } else {
            arrayOf("삭제", "즐겨찾기 삭제")
        }

        AlertDialog.Builder(this).apply {
            setItems(
                    dialogMessages
            ) { _, which ->
                presenter.selectDialogMenuOf(item, which)
            }
        }.create().show()
    }

}
