package it.aliut.iamdev.ilserpente.ui.game

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import it.aliut.iamdev.ilserpente.R
import it.aliut.iamdev.ilserpente.game.player.ComputerPlayer
import it.aliut.iamdev.ilserpente.game.player.Player
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.android.synthetic.main.game_fragment.view.*

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.game_fragment, container, false)

        layout.button_back.setOnClickListener {
            viewModel.endGame()
        }

        layout.game_surface.rows = 10
        layout.game_surface.columns = 10

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        val players = ArrayList<Player>()
        players.add(ComputerPlayer("(Computer) Player One", Color.RED))
        players.add(ComputerPlayer("(Computer) Player Two", Color.BLUE))

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

        viewModel.onEndGameEvent.observe(viewLifecycleOwner, Observer { gameEnded ->
            if (gameEnded) {
                AlertDialog.Builder(context!!)
                    .setMessage("Game finished")
                    .setPositiveButton("OK") { _, _ ->
                        activity!!.onBackPressed()
                    }
                    .create()
                    .show()
            }
        })

        viewModel.startGame(players)
    }

}
