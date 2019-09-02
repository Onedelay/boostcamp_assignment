package com.onedelay.boostcampassignment.liked

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import com.onedelay.boostcampassignment.main.MovieViewHolder
import com.onedelay.boostcampassignment.result.WebViewActivity
import com.onedelay.boostcampassignment.utils.Constants
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


internal class LikedMovieActivity : DaggerAppCompatActivity(), MovieViewHolder.ItemClickListener, LikedMovieContract.View {

    companion object {
        const val RESULT_CODE_DATA_CHANGED = 1000
    }

    @Inject lateinit var presenter: LikedMovieContract.Presenter

    @Inject lateinit var adapter: LikedMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked)

        initViews()
    }

    override fun onClick(item: MovieItemLookFeel) {
        val intent = Intent(this@LikedMovieActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }

        startActivity(intent)
    }

    override fun onLongClick(item: MovieItemLookFeel) {
        AlertDialog.Builder(this)
                .apply {
                    setItems(arrayOf("삭제"), DialogInterface.OnClickListener { _, which ->
                        presenter.selectDialogMenuOf(item, which)
                    })
                }
                .create()
                .show()
    }

    override fun showMovieList(likedList: List<MovieItemLookFeel>) {
        adapter.addItems(likedList)
    }

    override fun updateRemovedList(item: MovieItemLookFeel) {
        adapter.removeItem(item)

        setResult(RESULT_CODE_DATA_CHANGED)

        Toast.makeText(this, "즐겨찾기 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        initList()
    }

    private fun initList() {
        adapter.setListener(this)
        val linearLayoutManager = LinearLayoutManager(baseContext)

        recyclerView.apply {
            adapter       = this@LikedMovieActivity.adapter
            layoutManager = linearLayoutManager

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(this@LikedMovieActivity, linearLayoutManager.orientation))
        }

        presenter.requestLikeMovieList()
    }

}
