package com.example.myapplication.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class RecyclerViewDecorator(private val mBottomSpaceHeight: Int, private val dividerHeight: Int, dividerColor: Int) : ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = dividerColor
        paint.strokeWidth = dividerHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent.adapter?.itemCount ?: return

        for (i in 0 until itemCount - 1) {
            val child = parent.getChildAt(i) ?: continue
            val top = child.bottom
            val bottom = top + dividerHeight
            canvas.drawLine(child.left.toFloat(), bottom.toFloat(), child.right.toFloat(), bottom.toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.bottom = mBottomSpaceHeight
        } else {
            outRect.bottom = dividerHeight
        }
    }
}
