package com.example.numberpuzzles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

@SuppressLint("ViewConstructor")
class BoardView(context: Context?,private val boardView: Board) : View(context) {
    private var width = 0f
    private var height = 0f
    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width = (w / boardView.size()).toFloat()
        height = (h / boardView.size()).toFloat()
        super.onSizeChanged(w, h, oldw, oldh)
    }
    private fun locatePlace(x: Float, y: Float): Place? {
        val ix = (x / width).toInt()
        val iy = (y / height).toInt()
        return boardView.itemPlace(ix + 1, iy + 1)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event)
        val p = locatePlace(event.x, event.y)
        if (p != null && p.canSlide() && !boardView.solved()) {
            p.onSlide()
            invalidate()
        }
        return true
    }
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val background = Paint()
        background.color = resources.getColor(R.color.board_color)
        canvas.drawRect(0f, 0f, getWidth().toFloat(), getHeight().toFloat(), background)
        val dark = Paint()
        dark.color = resources.getColor(R.color.item_color2)
        dark.strokeWidth = 5f
        val dark1 = Paint()
        dark1.color = resources.getColor(R.color.item_color1)
        dark1.strokeWidth = 5f
        for (i in 0 until boardView.size()) {
            canvas.drawLine(0f, i * height, getWidth().toFloat(), i * height, dark1)
            canvas.drawLine(i * width, 0f, i * width, getHeight().toFloat(), dark1)
        }
        val foreground = Paint(Paint.ANTI_ALIAS_FLAG)
        foreground.color = resources.getColor(R.color.item_color)
        foreground.style = Paint.Style.FILL
        foreground.textSize = height * 0.75f
        foreground.textScaleX = width / height
        foreground.textAlign = Paint.Align.CENTER
        val x = width / 2
        val fm = foreground.fontMetrics
        val y = height / 2 - (fm.ascent + fm.descent) / 2
        val it = boardView.places().iterator()
        for (left in 0 until boardView.size()) {
            for (top in 0 until boardView.size()) {
                if (it.hasNext()) {
                    val p = it.next()
                    if (p.haveItem()) {
                        val number = p.item!!.itemNumber().toString()
                        canvas.drawText(number, left * width + x, top * height + y, foreground)
                    } else {
                        canvas.drawRect(left * width, top * height, left * width + width, top * height + height, dark)
                    }
                }
            }
        }
    }
}