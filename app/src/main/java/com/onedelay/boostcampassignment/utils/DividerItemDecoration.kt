package com.onedelay.boostcampassignment.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View

class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val dividerHeight: Int
    private val divider: Drawable?

    init {
        // 기본인 ListView 구분선의 Drawable 을 얻는다
        val ta = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        divider = ta.getDrawable(0)

        // 표시할 때마다 높이를 가져오지 않아도 되게 여기서 구해 둔다
        dividerHeight = divider?.intrinsicHeight ?: 0
        ta.recycle()
    }

    // RecyclerView 의 아이템마다 아래에 선을 그린다
    override fun onDraw(c: Canvas,
                        parent: RecyclerView,
                        state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        // 좌우의 padding 으로 선의 Start, End 설정
        val lineStart = parent.paddingStart
        val lineEnd = parent.width - parent.paddingEnd

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) { // -1 한 이유 : 마지막 요소 밑은 밑줄 표시 안함
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            // 애니메이션 등일 때에 제대로 이동하기 위해서
            val childTransitionY = Math.round(ViewCompat.getTranslationY(child))
            val top = child.bottom + params.bottomMargin + childTransitionY
            val bottom = top + dividerHeight

            // View 아래에 선을 그린다
            divider!!.setBounds(lineStart, top, lineEnd, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.set(0, 0, 0, dividerHeight)
    }
}
