package me.duncanleo.diporto.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator

/**
 * Created by duncanleo on 20/7/17.
 */
class RoomMembersView: LinearLayout {
    val TAG = "RoomMembersView"
    var members = mutableListOf<String>()
        get
        set(value) {
            field.clear()
            field.addAll(value)
            updateLayout()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        if (isInEditMode) {
            members = mutableListOf(
                    "John",
                    "Bob",
                    "Martin"
            )
        }
    }

    fun updateLayout() {
        Log.d(TAG, "Displaying ${members.size} members")
        removeAllViews()
        val colorGenerator = ColorGenerator.MATERIAL
        members.map {
            TextDrawable.builder()
                    .beginConfig()
                        .fontSize(50)
                        .width(100)
                        .height(100)
                        .bold()
                    .endConfig()
                    .buildRound(it[0].toString(), colorGenerator.getColor(it))
        }.map {
            val imageView = ImageView(context)
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            lp.rightMargin = 10
            imageView.layoutParams = lp
            imageView.adjustViewBounds = true
            imageView.setImageDrawable(it)
            imageView
        }.forEach {
            addView(it)
        }
    }
}
