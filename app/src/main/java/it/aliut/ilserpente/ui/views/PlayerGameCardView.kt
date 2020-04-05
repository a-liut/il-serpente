package it.aliut.ilserpente.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import it.aliut.ilserpente.R
import it.aliut.ilserpente.game.player.HumanPlayer
import it.aliut.ilserpente.game.player.Player
import kotlinx.android.synthetic.main.player_gamecard_view.view.*

class PlayerGameCardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var player: Player? = null
        set(value) {
            field = value

            when (value) {
                is HumanPlayer -> updateUI(value)
                else -> updateUI(value)
            }
        }

    var isPlayerActive: Boolean = false
        set(value) {
            field = value
            image_player_active.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    init {
        View.inflate(context, R.layout.player_gamecard_view, this)
    }

    private fun updateUI(player: HumanPlayer?) {
        text_player_name.text = player?.user?.name ?: ""
        if (player?.user?.photoUrl != null) {
            Glide.with(this).load(player.user.photoUrl).into(image_player_avatar)
        } else {
            image_player_avatar.setColorFilter(player?.color ?: Color.BLACK)
        }
    }

    private fun updateUI(player: Player?) {
        text_player_name.text = player?.name ?: ""
        image_player_avatar.setColorFilter(player?.color ?: Color.BLACK)
    }
}
