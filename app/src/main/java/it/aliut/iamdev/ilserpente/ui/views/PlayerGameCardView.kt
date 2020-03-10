package it.aliut.iamdev.ilserpente.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import it.aliut.iamdev.ilserpente.R
import kotlinx.android.synthetic.main.player_gamecard_view.view.*

class PlayerGameCardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var playerName: String
        get() = text_player_name.text.toString()
        set(value) {
            text_player_name.text = value
        }

    var playerColor: Int
        get() = image_player_color.solidColor
        set(value) = image_player_color.setColorFilter(value)

    init {
        View.inflate(context, R.layout.player_gamecard_view, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PlayerGameCardView)

        playerName = attributes.getString(R.styleable.PlayerGameCardView_playerName) ?: ""
        image_player_active.visibility =
            if (attributes.getBoolean(
                    R.styleable.PlayerGameCardView_isActive,
                    false
                )
            ) View.VISIBLE else View.INVISIBLE

        image_player_color.setColorFilter(
            attributes.getColor(
                R.styleable.PlayerGameCardView_playerColor,
                Color.BLACK
            )
        )

        attributes.recycle()
    }
}