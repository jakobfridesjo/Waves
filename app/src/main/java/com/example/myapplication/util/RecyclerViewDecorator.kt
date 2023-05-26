package com.example.myapplication.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * The point of this custom decorator is to display an offset at the end of the recycler view list
 */
class RecyclerViewDecorator(
        private val mBottomSpaceHeight: Int,
        private val dividerHeight: Int
    ) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.bottom = mBottomSpaceHeight
        } else {
            outRect.bottom = dividerHeight
        }
    }
}
