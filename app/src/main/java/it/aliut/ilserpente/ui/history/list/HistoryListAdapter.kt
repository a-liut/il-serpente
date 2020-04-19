package it.aliut.ilserpente.ui.history.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.aliut.ilserpente.R
import it.aliut.ilserpente.model.MatchResult
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.history_item.view.*

class HistoryListAdapter(private val list: MutableList<MatchResult>) :
    RecyclerView.Adapter<HistoryListAdapter.HistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)

        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val item = list[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    class HistoryItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(matchResult: MatchResult) {
            with(itemView) {
                text_players.text = resources.getString(
                    R.string.history_players_text,
                    matchResult.players.joinToString(", ") { it.name })

                text_winner.text =
                    resources.getString(R.string.history_winner_text, matchResult.winner.name)

                text_moves.text =
                    resources.getString(R.string.history_moves_text, matchResult.moves)

                text_date.text =
                    resources.getString(
                        R.string.history_date_text,
                        SimpleDateFormat.getInstance().format(matchResult.date)
                    )
            }
        }
    }
}
