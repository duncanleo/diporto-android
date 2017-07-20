package me.duncanleo.diporto.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import me.duncanleo.diporto.R
import me.duncanleo.diporto.adapter.RoomsRecyclerViewAdapter
import me.duncanleo.diporto.model.Room
import me.duncanleo.diporto.network.Network

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val data = mutableListOf<Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roomsRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        roomsRecyclerView.addItemDecoration(DividerItemDecoration(baseContext, DividerItemDecoration.VERTICAL))

        swipeRefreshLayout.isRefreshing = true

        val adapter = RoomsRecyclerViewAdapter(data)
        adapter.setHasStableIds(true)
        roomsRecyclerView.adapter = adapter

        Network.getDiportoService().getRooms()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rooms ->
                    data.clear()
                    data.addAll(rooms)
                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
                }, { error ->
                    Log.d(TAG, "Error fetching rooms", error)
                    swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_create_room -> {
                MaterialDialog.Builder(this@MainActivity)
                        .title(R.string.label_create_room)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .cancelable(true)
                        .positiveText(R.string.label_create)
                        .negativeText(R.string.label_cancel)
                        .input(getString(R.string.label_room_name), "", { _, input ->
                            Log.d(TAG, "Creating room: '$input'")
                        })
                        .show()
            }
            R.id.menu_join_room -> {
                MaterialDialog.Builder(this@MainActivity)
                        .title(R.string.label_join_room)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .cancelable(true)
                        .positiveText(R.string.label_join)
                        .negativeText(R.string.label_cancel)
                        .input(getString(R.string.label_room_short_code), "", { _, input ->
                            Log.d(TAG, "Joining room: '$input'")
                        })
                        .show()
            }
        }
        return false
    }
}
