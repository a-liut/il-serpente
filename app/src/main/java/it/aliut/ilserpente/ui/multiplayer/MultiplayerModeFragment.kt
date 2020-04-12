package it.aliut.ilserpente.ui.multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import it.aliut.ilserpente.R
import kotlinx.android.synthetic.main.multiplayermode_fragment.*
import kotlinx.android.synthetic.main.multiplayermode_fragment.view.*

class MultiplayerModeFragment : Fragment(), View.OnClickListener {

    private val viewModel: MultiplayerModeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.multiplayermode_fragment, container, false)

        layout.button_random_opponent.setOnClickListener(this)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            progress.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
            button_random_opponent.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(button_random_opponent, message, Snackbar.LENGTH_LONG).show()
        })

        viewModel.startGameEvent.observe(viewLifecycleOwner, Observer { gameData ->
            findNavController()
                .navigate(
                    MultiplayerModeFragmentDirections.actionMultiplayerModeFragmentToGameFragment(
                        gameData
                    )
                )
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            button_random_opponent.id -> viewModel.startRandom()
        }
    }
}
