package it.aliut.iamdev.ilserpente

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import it.aliut.iamdev.ilserpente.game.Board
import it.aliut.iamdev.ilserpente.game.GameEngine
import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState
import it.aliut.iamdev.ilserpente.game.player.Player
import org.amshove.kluent.shouldBeEqualTo
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.Test

@ExperimentalStdlibApi
class GameEngineTest {

    @Test
    fun `should player one win`() {
        val mockedPlayerOne: Player = mockk {
            coEvery { getNextMove(any()) } returns GameMove.RIGHT
        }

        val mockedPlayerTwo: Player = mockk {
            coEvery { getNextMove(any()) } returns GameMove.DOWN
        }

        val allowedMoves = mutableListOf(
            listOf(
                GameMove.DOWN,
                GameMove.UP,
                GameMove.LEFT,
                GameMove.RIGHT
            ),
            listOf()
        )

        val mockedBoard: Board = mockk {
            every { startPosition } returns Pair(10, 10)
            every { moves } returns arrayListOf()
            every { applyMove(any()) } just Runs
            every { allowedMoves() } returnsMany allowedMoves
        }

        val mockedGameState = mockk<GameState>(relaxUnitFun = true) {
            every { movesCount } returns 2
            every { board } returns mockedBoard
            every { applyMove(any()) } returns this
            every { isValidMove(any()) } returns true
        }

        val mockedCallback = mockk<GameEngine.Callback>(relaxed = true) {}

        val gameEngine = GameEngine(
            players = listOf(mockedPlayerOne, mockedPlayerTwo),
            gameState = mockedGameState,
            callback = mockedCallback
        )

        gameEngine.start()

        await.forever() until { gameEngine.gameEnded }

        gameEngine.currentPlayer shouldBeEqualTo mockedPlayerOne
    }

    @Test
    fun `should player two win`() {
        val mockedPlayerOne: Player = mockk {
            coEvery { getNextMove(any()) } returns GameMove.RIGHT
        }

        val mockedPlayerTwo: Player = mockk {
            coEvery { getNextMove(any()) } returns GameMove.DOWN
        }

        val allowedMoves = mutableListOf(
            listOf(
                GameMove.DOWN,
                GameMove.UP,
                GameMove.LEFT,
                GameMove.RIGHT
            ),
            listOf(
                GameMove.DOWN,
                GameMove.UP,
                GameMove.LEFT,
                GameMove.RIGHT
            ),
            listOf()
        )

        val mockedBoard: Board = mockk {
            every { startPosition } returns Pair(10, 10)
            every { moves } returns arrayListOf()
            every { applyMove(any()) } just Runs
            every { allowedMoves() } returnsMany allowedMoves
        }

        val mockedGameState = mockk<GameState>(relaxUnitFun = true) {
            every { movesCount } returns 2
            every { board } returns mockedBoard
            every { applyMove(any()) } returns this
            every { isValidMove(any()) } returns true
        }

        val mockedCallback = mockk<GameEngine.Callback>(relaxed = true) {}

        val gameEngine = GameEngine(
            players = listOf(mockedPlayerOne, mockedPlayerTwo),
            gameState = mockedGameState,
            callback = mockedCallback
        )

        gameEngine.start()

        await.forever() until { gameEngine.gameEnded }

        gameEngine.currentPlayer shouldBeEqualTo mockedPlayerTwo
    }
}
