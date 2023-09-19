package com.example.detachimage.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.detachimage.R
import getAppColor

class CV4 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "DetectImage"
    }

    // pass bitmap image
    private val src: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.img)

    private var left = 10f
    private var right = 0f

    private var screenWidth = 0
    private var imageWidth = 0
    private var imageHeight = 0

    //get the bitmap from test():Bitmap? function
    private lateinit var output: Bitmap

    // p for point and l=left t=top r=right b=bottom
    private var pl = 0f
    private var pt = 0f
    private var pr = 0f
    private var pb = 0f

    // resizable rect
    private var rectF: RectF? = null

    private val rectPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = getAppColor(R.color.transparent)
    }

    private val rectPaint5 = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = 20f
        color = getAppColor(R.color.red_new)
    }

    private val paintQuestion = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = 1f
        color = Color.argb(170, 0, 0, 0)
    }

    // dark overlay rect
    private var rectF2 = RectF(0f, 0f, screenWidth.toFloat(), imageHeight.toFloat())
    private val rectPaint2 = Paint().apply {
        style = Paint.Style.FILL
        color = Color.argb(150, 0, 0, 0)
    }

    private val paintText = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        textSize = 32f
        color = getAppColor(R.color.white)
    }

    private val ovalPaint = Paint().apply {
        style = Paint.Style.FILL
        color = getAppColor(R.color.black)
    }

    private val listCoodinate: MutableMap<Int, Pair<Float, Float>> = hashMapOf()

    private var isSelect = false

    private var index = 0

    init {
//        val pairFirst = Pair(70f, 260f)
//        val pairSecond = Pair(280f, 480f)
//        val pairThird = Pair(520f, 710f)
//        val pairFour = Pair(740f, 930f)
//        val pairFive = Pair(960f, 1140f)
//
//        listCoodinate[0] = pairFirst
//        listCoodinate[1] = pairSecond
//        listCoodinate[2] = pairThird
//        listCoodinate[3] = pairFour
//        listCoodinate[4] = pairFive
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        screenWidth = MeasureSpec.getSize(widthMeasureSpec)
        imageWidth = screenWidth
        imageHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(screenWidth, imageHeight)
        Log.d(TAG, "onMeasure: ")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: ")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initCoodinate()
        Log.d(TAG, "onSizeChanged: ")
        val desiredWidthInPx = imageWidth
        val derivedHeightInPx = imageHeight
        output = Bitmap.createScaledBitmap(src, desiredWidthInPx, derivedHeightInPx, true)
        rectF2 = RectF(0f, 0f, imageWidth.toFloat(), derivedHeightInPx.toFloat())
        rectF = RectF(50f, listCoodinate[1]!!.first, imageWidth - 50f, listCoodinate[1]!!.second)
        right = screenWidth - 10f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw: ")
        canvas?.apply {

            // background image under overlay
            drawBitmap(output, 0f, 0f, null)

            // dark overlay
            drawRect(rectF2, rectPaint2)

            // drawRect(rectF!!, paintQuestion)

            // drawOval(rectF!!, ovalPaint)

            drawListRectAndOval(this)

            if (isSelect) {
                val rectSelect =
                    RectF(left, listCoodinate[index]!!.first, right, listCoodinate[index]!!.second)
                clipRect(rectSelect)
                drawBitmap(output, 0f, 0f, null)
                drawRect(rectSelect, rectPaint)
                drawLine(this)
                isSelect = false
            }

            // drawLine(100f, 100f, 200f, 200f, rectPaint5)
        }
    }

    private fun initCoodinate() {
        val pairFirst = Pair(70f, 260f)
        val pairSecond = Pair(280f, 480f)
        val pairThird = Pair(520f, 710f)
        val pairFour = Pair(740f, 930f)
        val pairFive = Pair(960f, 1140f)

        listCoodinate[0] = pairFirst
        listCoodinate[1] = pairSecond
        listCoodinate[2] = pairThird
        listCoodinate[3] = pairFour
        listCoodinate[4] = pairFive
    }

    private fun drawListRectAndOval(canvas: Canvas?) {
        listCoodinate.forEach { (k, v) ->
            val rectF = RectF(left, v.first + 5, right, v.second - 5)
            canvas?.drawRect(rectF, paintQuestion)
            val middle = (v.second - v.first) / 2
            val rectOval = RectF(
                screenWidth.toFloat() / 2 - 40,
                v.first + middle - 40,
                screenWidth.toFloat() / 2 + 40,
                v.second - middle + 40
            )
            canvas?.drawOval(rectOval, ovalPaint)
            canvas?.drawText(
                (k + 1).toString(),
                screenWidth.toFloat() / 2 - 10,
                v.second - middle + 10,
                paintText
            )
        }
    }

    private fun drawLine(canvas: Canvas?) {
        canvas?.apply {
            drawLine(
                left,
                listCoodinate[index]!!.first,
                left + 40,
                listCoodinate[index]!!.first,
                rectPaint5
            )

            drawLine(
                left,
                listCoodinate[index]!!.first,
                left,
                listCoodinate[index]!!.first + 40,
                rectPaint5
            )

            drawLine(
                left,
                listCoodinate[index]!!.second,
                left + 40,
                listCoodinate[index]!!.second,
                rectPaint5
            )

            drawLine(
                left,
                listCoodinate[index]!!.second,
                left,
                listCoodinate[index]!!.second - 40,
                rectPaint5
            )

            drawLine(
                right,
                listCoodinate[index]!!.first,
                right - 40,
                listCoodinate[index]!!.first,
                rectPaint5
            )

            drawLine(
                right,
                listCoodinate[index]!!.first,
                right,
                listCoodinate[index]!!.first + 40,
                rectPaint5
            )

            drawLine(
                right,
                listCoodinate[index]!!.second,
                right - 40,
                listCoodinate[index]!!.second,
                rectPaint5
            )

            drawLine(
                right,
                listCoodinate[index]!!.second,
                right,
                listCoodinate[index]!!.second - 40,
                rectPaint5
            )
        }
    }

    fun update(index: Int) {
        isSelect = true
        this.index = index
        invalidate()
    }
}
