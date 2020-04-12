package it.aliut.ilserpente.ui.modeselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import it.aliut.ilserpente.R
import kotlinx.android.synthetic.main.modeselection_fragment.*
import kotlinx.android.synthetic.main.modeselection_fragment.view.*
import timber.log.Timber

class ModeSelectionFragment : Fragment(), View.OnClickListener {

    private val viewModel: ModeSelectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.modeselection_fragment, container, false)

        layout.button_single.setOnClickListener(this)
        layout.button_1v1.setOnClickListener(this)
        layout.button_multiplayer.setOnClickListener(this)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.startGameEvent.observe(viewLifecycleOwner, Observer { gameData ->
            findNavController().navigate(
                ModeSelectionFragmentDirections.actionModeSelectionFragmentToGameFragment(
                    gameData
                )
            )
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            button_single.id -> viewModel.startSingle()
            button_1v1.id -> viewModel.start1v1()
            button_multiplayer.id ->
                findNavController().navigate(ModeSelectionFragmentDirections.actionModeSelectionFragmentToMultiplayerModeFragment())
            else -> Timber.d("This should never happen")
        }
    }
}
