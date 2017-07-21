package me.duncanleo.diporto.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import kotlinx.android.synthetic.main.item_review.view.*
import me.duncanleo.diporto.R
import me.duncanleo.diporto.activity.RoomActivity
import me.duncanleo.diporto.model.Review
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by duncanleo on 17/7/17.
 */
class ReviewsRecyclerViewAdapter : RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {
    private val data: List<Review>

    constructor(data: List<Review>) : super() {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_review, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.name?.text = data[position].user.name
        holder?.body?.text = data[position].text
        holder?.date?.text = "${TimeUnit.MILLISECONDS.toDays(Date().time - data[position].time.time)}d ago"
        holder?.rating?.rating = data[position].rating.toFloat()

        val iconDrawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(50)
                .width(100)
                .height(100)
                .bold()
                .endConfig()
                .buildRound(data[position].user.name[0].toString(), ColorGenerator.MATERIAL.getColor(data[position].user.name))
        holder?.avatar?.setImageDrawable(iconDrawable)
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val name: TextView? = itemView?.authorTextView
        val avatar: ImageView? = itemView?.authorImageView
        val date: TextView? = itemView?.dateTextView
        val body: TextView? = itemView?.bodyTextView
        val rating: RatingBar? = itemView?.ratingBar
    }
}
