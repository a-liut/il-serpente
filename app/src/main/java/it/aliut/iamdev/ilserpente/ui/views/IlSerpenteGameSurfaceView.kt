package it.aliut.iamdev.ilserpente.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import it.aliut.iamdev.ilserpente.game.Board
import it.aliut.iamdev.ilserpente.game.GameMove

class IlSerpenteGameSurfaceView(context: Context, attributeSet: AttributeSet) :
    SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    var rows: Int = 0
        set(value) {
            if (value < 0) throw RuntimeException("Invalid row count")
            field = value
        }

    var columns: Int = 0
        set(value) {
            if (value < 0) throw RuntimeException("Invalid column count")
            field = value
        }

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        holder?.let {
            it.lockCanvas().let { canvas ->
                drawGrid(canvas)

                it.unlockCanvasAndPost(canvas)
            }
        }
    }

    /**
     * Draws the grid on the surface
     */
    private fun drawGrid(canvas: Canvas) {
        val backgroundPaint = Paint().apply { color = Color.WHITE } // White
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val linePaint = Paint().apply { color = Color.GRAY;alpha = 125 }

        val deltaY = width / (rows + 1)
        val deltaX = height / (columns + 1)

        for (current in deltaY until width - deltaY step deltaY) {
            canvas.drawLine(
                0f,
                current.toFloat(),
                width.toFloat(),
                current.toFloat(),
                linePaint
            )
        }

        for (current in deltaX until height - deltaY step deltaX) {
            canvas.drawLine(
                current.toFloat(),
                0f,
                current.toFloat(),
                height.toFloat(),
                linePaint
            )
        }
    }

    fun updateBoard(board: Board) {
        holder.lockCanvas()?.let { canvas ->
            drawGrid(canvas)

            val deltaX = width / (rows + 1)
            val deltaY = height / (columns + 1)

            var current = Pair(
                deltaX * board.startPosition.first,
                deltaY * board.startPosition.second
            )

            // Draw starting point
            canvas.drawCircle(
                current.first.toFloat(),
                current.second.toFloat(),
                5f,
                Paint().apply { color = Color.GREEN })

            for (move in board.moves) {
                val next = when (move.gameMove) {
                    GameMove.UP -> current.copy(second = current.second - deltaY)
                    GameMove.DOWN -> current.copy(second = current.second + deltaY)
                    GameMove.LEFT -> current.copy(first = current.first - deltaX)
                    GameMove.RIGHT -> current.copy(first = current.first + deltaX)
                }

                canvas.drawLine(
                    current.first.toFloat(),
                    current.second.toFloat(),
                    next.first.toFloat(),
                    next.second.toFloat(),
                    Paint().apply { color = move.player.color })

                current = next
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }
}