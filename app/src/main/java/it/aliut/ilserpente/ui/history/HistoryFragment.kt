package it.aliut.ilserpente.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import it.aliut.ilserpente.R
import it.aliut.ilserpente.game.MatchResult
import it.aliut.ilserpente.ui.history.list.HistoryListAdapter
import kotlinx.android.synthetic.main.history_fragment.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModel()

    private val historyList = mutableListOf<MatchResult>()

    private val historyListAdapter = HistoryListAdapter(historyList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.history_fragment, container, false)

        layout.history_list.setHasFixedSize(true)
        layout.history_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layout.history_list.adapter = historyListAdapter
        layout.history_list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.historyList.observe(viewLifecycleOwner, Observer { list ->
            updateList(list)
        })
    }

    private fun updateList(newList: List<MatchResult>) {
        historyList.clear()
        historyList.addAll(newList)
        historyListAdapter.notifyDataSetChanged()
    }
}
