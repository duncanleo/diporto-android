package me.duncanleo.diporto.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.duncanleo.diporto.R
import me.duncanleo.diporto.model.Room
import kotlinx.android.synthetic.main.item_room.view.*
import me.duncanleo.diporto.activity.RoomActivity

/**
 * Created by duncanleo on 17/7/17.
 */
class RoomsRecyclerViewAdapter : RecyclerView.Adapter<RoomsRecyclerViewAdapter.ViewHolder> {
    private val data: List<Room>

    constructor(data: List<Room>) : super() {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_room, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.itemView?.setOnClickListener {
            val intent = Intent(holder.itemView.context, RoomActivity::class.java)
            intent.putExtra(RoomActivity.roomKey, data[position])
            holder.itemView?.context?.startActivity(intent)
        }
        holder?.name?.text = data[position].name
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val name: TextView? = itemView?.roomNameTextView
    }
}
