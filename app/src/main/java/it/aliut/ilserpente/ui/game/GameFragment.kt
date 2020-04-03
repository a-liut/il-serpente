package it.aliut.ilserpente.ui.game

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import it.aliut.ilserpente.R
import it.aliut.ilserpente.game.GameMove
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.android.synthetic.main.game_fragment.view.*
import timber.log.Timber

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    private val args: GameFragmentArgs by navArgs()

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.game_fragment, container, false)

        layout.button_back.setOnClickListener {
            viewModel.endGame()
            viewModel.exitGame()
        }

        layout.game_surface.rows = GameViewModel.DEFAULT_ROW_COUNT
        layout.game_surface.columns = GameViewModel.DEFAULT_COLUMN_COUNT

        gestureDetector = GestureDetectorCompat(
            context,
            SwipeGestureListener()
        )

        layout.game_surface.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val players = args.gamedata.players

        viewModel.players.observe(viewLifecycleOwner, Observer { gamePlayers ->
            player_card_one.player = gamePlayers[0]
            player_card_two.player = gamePlayers[1]
        })

        viewModel.currentPlayer.observe(viewLifecycleOwner, Observer { player ->
            if (player == players[0]) {
                player_card_one.isPlayerActive = true
                player_card_two.isPlayerActive = false
            } else {
                player_card_one.isPlayerActive = false
                player_card_two.isPlayerActive = true
            }
        })

        viewModel.gameState.observe(viewLifecycleOwner, Observer { state ->
            text_moves_counter.text = state.movesCount.toString()

            game_surface.updateBoard(state.board)
        })

        viewModel.onEndGameEvent.observe(viewLifecycleOwner, Observer {
            Timber.d("Game ended!")
        })

        viewModel.onExitGameEvent.observe(viewLifecycleOwner, Observer { canExit ->
            if (canExit) {
                findNavController().navigateUp()
            }
        })

        viewModel.onInvalidMove.observe(viewLifecycleOwner, Observer {
            Snackbar.make(game_surface, "Move not allowed! Try again.", Snackbar.LENGTH_SHORT)
                .show()
        })

        viewModel.winner.observe(viewLifecycleOwner, Observer { winner ->
            AlertDialog.Builder(context!!)
                .setMessage("${winner.name} won!")
                .setPositiveButton("OK") { _, _ -> }
                .create()
                .show()
        })

        viewModel.startGame(players)
    }

    inner class SwipeGestureListener :
        GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            downEvent: MotionEvent,
            upEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Timber.d("onFling: \n$downEvent\n$upEvent")

            getGameMove(downEvent, upEvent).let {
                viewModel.triggerPlayerMove(it)
            }.also {
                Timber.d("GameMove: $it")
            }

            return super.onFling(downEvent, upEvent, velocityX, velocityY)
        }

        private fun getGameMove(downEvent: MotionEvent, upEvent: MotionEvent): GameMove {
            val pi4 = PI / 4

            val (dx, dy, a) =
                if (downEvent.x <= upEvent.x && downEvent.y >= upEvent.y)
                    Triple(
                        upEvent.x - downEvent.x,
                        downEvent.y - upEvent.y,
                        0.toDouble()
                    )
                else if (downEvent.x >= upEvent.x && downEvent.y >= upEvent.y)
                    Triple(
                        downEvent.x - upEvent.x,
                        downEvent.y - upEvent.y,
                        -PI
                    )
                else if (downEvent.x >= upEvent.x && downEvent.y <= upEvent.y)
                    Triple(
                        downEvent.x - upEvent.x,
                        upEvent.y - downEvent.y,
                        PI
                    )
                else
                    Triple(
                        upEvent.x - downEvent.x,
                        upEvent.y - downEvent.y,
                        -2 * PI
                    )

            val r = sqrt(dy.pow(2) + dx.pow(2))

            val sin = dy / r
            val cos = dx / r

            val tan = sin.toDouble() / cos.toDouble()

            val angle = abs(atan(tan) + a)

            Timber.d("angle: $angle")

            return if (angle < pi4 || angle > (2 * Math.PI - pi4))
                GameMove.RIGHT
            else if (angle < 3 * pi4)
                GameMove.UP
            else if (angle < 6 * pi4)
                GameMove.LEFT
            else
                GameMove.DOWN
        }
    }
}
