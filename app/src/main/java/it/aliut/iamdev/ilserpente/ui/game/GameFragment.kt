package it.aliut.iamdev.ilserpente.ui.game

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

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        viewModel.players.observe(viewLifecycleOwner, Observer { players ->
            player_card_one.playerName = players[0].name
            player_card_two.playerName = players[1].name
        })

        viewModel.gameState.observe(viewLifecycleOwner, Observer { state ->
            text_moves_counter.text = state.movesCount.toString()
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

        val players = ArrayList<Player>()
        players.add(ComputerPlayer("(Computer) Player One"))
        players.add(ComputerPlayer("(Computer) Player Two"))

        viewModel.startGame(players)
    }

}
