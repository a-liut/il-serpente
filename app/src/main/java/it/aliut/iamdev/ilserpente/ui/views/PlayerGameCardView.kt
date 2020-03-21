package it.aliut.iamdev.ilserpente.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import it.aliut.iamdev.ilserpente.R
import it.aliut.iamdev.ilserpente.game.player.Player
import kotlinx.android.synthetic.main.player_gamecard_view.view.*

class PlayerGameCardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var player: Player? = null
        set(value) {
            field = value
            text_player_name.text = value?.name ?: ""
            image_player_color.setColorFilter(value?.color ?: Color.BLACK)
        }

    var isPlayerActive: Boolean = false
        set(value) {
            field = value
            image_player_active.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    init {
        View.inflate(context, R.layout.player_gamecard_view, this)
    }
}
