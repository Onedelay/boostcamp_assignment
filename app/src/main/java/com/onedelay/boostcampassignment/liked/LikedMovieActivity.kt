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
import kotlinx.android.synthetic.main.activity_main.*


class LikedMovieActivity : AppCompatActivity(), MovieViewHolder.ItemClickListener, LikedMovieContract.View {

    private lateinit var adapter: LikedMovieAdapter

    private lateinit var presenter: LikedMovieContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked)

        presenter = LikedMoviePresenter(this, InMemoryDataHolder)

        initViews()
    }

    override fun onClick(item: MovieItemLookFeel) {
        val intent = Intent(this@LikedMovieActivity, WebViewActivity::class.java).apply {
            putExtra(Constants.URL, item.link)
        }
        startActivity(intent)
    }

    override fun onLongClick(item: MovieItemLookFeel) {
        val builder = AlertDialog.Builder(this).apply {
            setItems(
                    arrayOf("삭제"),
                    DialogInterface.OnClickListener { _, which ->
                        presenter.selectDialogMenuOf(item, which)
                    })
        }
        builder.create().show()
    }

    override fun showMovieList(likedList: List<MovieItemLookFeel>) {
        adapter.addItems(likedList)
    }

    override fun updateRemovedList(item: MovieItemLookFeel) {
        adapter.removeItem(item)
        Toast.makeText(this, "즐겨찾기 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        initList()
    }

    private fun initList() {
        adapter = LikedMovieAdapter(this)

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