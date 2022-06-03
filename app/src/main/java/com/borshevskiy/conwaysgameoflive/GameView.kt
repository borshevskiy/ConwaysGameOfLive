package com.borshevskiy.conwaysgameoflive

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.WindowManager

class GameView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs),
    Runnable {

    private var thread: Thread? = null
    private var isRunning = false
    private var columns = 0
    private var rows = 0
    private var isInitialized = false
    private lateinit var gameField: GameField
    private val rect by lazy { Rect() }
    private val paint by lazy { Paint() }

    override fun run() {
        while (isRunning) {
            if (!holder.surface.isValid) continue
            try {
                Thread.sleep(STEP_TIME)
            } catch (e: InterruptedException) {
            }
            with(holder.lockCanvas()) {
                if (!isInitialized) {
                    initWorld()
                    isInitialized = true
                }
                gameField.generateLifecycle()
                drawCells(this)
                holder.unlockCanvasAndPost(this)
            }
        }
    }

    fun start() {
        isRunning = true
        thread = Thread(this)
        thread?.start()
    }

    fun randomize() {
        isRunning = false
        isInitialized = true
        with(holder.lockCanvas()) {
            initWorld()
            drawCells(this)
            holder.unlockCanvasAndPost(this)
        }
    }

    fun stop() {
        isRunning = false
        while (true) {
            try {
                thread!!.join()
            } catch (e: InterruptedException) {}
            break
        }
    }

    private fun initWorld() {
        val point = Point()
        (context.getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
        columns = point.x / CUSTOM_WIDTH_SIZE
        rows = point.y / CUSTOM_HEIGHT_SIZE
        gameField = GameField(WIDTH, HEIGHT)
    }

    private fun drawCells(canvas: Canvas) {
        for (i in 0 until WIDTH) {
            for (j in 0 until HEIGHT) {
                val cell = gameField.get(i, j)
                rect[cell.x * columns - 1, cell.y * rows - 1, cell.x * columns + columns - 1] =
                    cell.y * rows + rows - 1
                paint.color = if (cell.isAlive) Color.BLACK else Color.WHITE
                canvas.drawRect(rect, paint)
            }
        }
    }

    companion object {
        private const val CUSTOM_WIDTH_SIZE = 50
        private const val CUSTOM_HEIGHT_SIZE = 60
        private const val WIDTH = 50
        private const val HEIGHT = 50
        private const val STEP_TIME = 250L
    }
}