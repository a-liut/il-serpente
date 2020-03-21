package it.aliut.iamdev.ilserpente.game

import timber.log.Timber

class Board(
    private val rows: Int,
    private val columns: Int,
    val startPosition: Pair<Int, Int> = Pair(rows / 2, columns / 2)
) {

    private var currentPosition: Pair<Int, Int> = startPosition

    private val cells: HashSet<Pair<Int, Int>> = HashSet()

    val moves: ArrayList<PlayerMove> = arrayListOf()

    init {
        cells.add(currentPosition)
    }

    fun allowedMoves(): List<GameMove> = arrayListOf<GameMove>().apply {
        val leftPos = currentPosition.copy(first = currentPosition.first - 1)
        val rightPos = currentPosition.copy(first = currentPosition.first + 1)
        val upPos = currentPosition.copy(second = currentPosition.second - 1)
        val downPos = currentPosition.copy(second = currentPosition.second + 1)

        if (currentPosition.first > 1 && !cells.contains(leftPos)) {
            add(GameMove.LEFT)
        }

        if (currentPosition.first < columns && !cells.contains(rightPos)) {
            add(GameMove.RIGHT)
        }

        if (currentPosition.second > 1 && !cells.contains(upPos)) {
            add(GameMove.UP)
        }

        if (currentPosition.second < rows && !cells.contains(downPos)) {
            add(GameMove.DOWN)
        }
    }

    fun applyMove(playerMove: PlayerMove) {
        val move = playerMove.gameMove

        if (!allowedMoves().contains(move)) {
            throw MoveNotAllowedException(move)
        }

        currentPosition = when (move) {
            GameMove.UP -> {
                currentPosition.copy(second = currentPosition.second - 1)
            }
            GameMove.DOWN -> {
                currentPosition.copy(second = currentPosition.second + 1)
            }
            GameMove.LEFT -> {
                currentPosition.copy(first = currentPosition.first - 1)
            }
            GameMove.RIGHT -> {
                currentPosition.copy(first = currentPosition.first + 1)
            }
        }

        onMoveCompleted(playerMove, currentPosition)

        Timber.d("currentPosition: $currentPosition")
    }

    private fun onMoveCompleted(playerMove: PlayerMove, position: Pair<Int, Int>) {
        moves.add(playerMove)
        cells.add(position)
    }
}
