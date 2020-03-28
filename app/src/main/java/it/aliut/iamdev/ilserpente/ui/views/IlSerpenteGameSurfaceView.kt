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

    private var lastBoard: Board? = null

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        draw()
    }

    fun updateBoard(board: Board) {
        lastBoard = board
        draw()
    }

    private fun draw() {
        holder?.let { surface ->
            surface.lockCanvas().let { canvas ->
                drawGrid(canvas)

                lastBoard?.let { drawBoard(canvas, it) }

                surface.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        val backgroundPaint = Paint().apply { color = Color.WHITE } // White
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val linePaint = Paint().apply { color = Color.GRAY; alpha = 125 }

        val deltaY = height / (rows + 1)
        val deltaX = width / (columns + 1)

        for (current in deltaY until height - deltaY step deltaY) {
            canvas.drawLine(
                0f,
                current.toFloat(),
                width.toFloat(),
                current.toFloat(),
                linePaint
            )
        }

        for (current in deltaX until width - deltaX step deltaX) {
            canvas.drawLine(
                current.toFloat(),
                0f,
                current.toFloat(),
                height.toFloat(),
                linePaint
            )
        }
    }

    private fun drawBoard(canvas: Canvas, board: Board) {
        val deltaY = height / (rows + 1)
        val deltaX = width / (columns + 1)

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
    }
}
