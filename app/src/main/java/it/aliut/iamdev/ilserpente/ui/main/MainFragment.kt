package it.aliut.iamdev.ilserpente.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import it.aliut.iamdev.ilserpente.R
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment(), View.OnClickListener {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.main_fragment, container, false)

        layout.button_start.setOnClickListener(this)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.startEvent.observe(viewLifecycleOwner, Observer { startGame ->
            if (startGame) {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToModeSelectionFragment())
            }
        })
    }

    override fun onClick(view: View?) {
        viewModel.onStartGame()
    }
}
