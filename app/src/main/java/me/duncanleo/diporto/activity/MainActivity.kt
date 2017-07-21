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
import me.duncanleo.diporto.network.payload.CreateRoomPayload

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val data = mutableListOf<Room>()
    private lateinit var adapter: RoomsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roomsRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        roomsRecyclerView.addItemDecoration(DividerItemDecoration(baseContext, DividerItemDecoration.VERTICAL))

        adapter = RoomsRecyclerViewAdapter(data)
        adapter.setHasStableIds(true)
        roomsRecyclerView.adapter = adapter

        load()
        swipeRefreshLayout.setOnRefreshListener {
            load()
        }
    }

    fun load() {
        swipeRefreshLayout.isRefreshing = true
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
                            Network.getDiportoService().createRoom(CreateRoomPayload(
                                    name = input.toString()
                            ))
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ _ ->
                                        load()
                                    }, { error ->
                                        Log.d(TAG, "error occured creating room", error)
                                        MaterialDialog.Builder(this@MainActivity)
                                                .title(R.string.label_error)
                                                .content("Error creating $input")
                                                .positiveText(R.string.label_ok)
                                                .show()
                                    })
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
                            Network.getDiportoService().joinRoom(input.toString())
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ _ ->
                                        load()
                                        MaterialDialog.Builder(this@MainActivity)
                                                .title(R.string.label_joined_room)
                                                .content("Joined $input")
                                                .positiveText(R.string.label_ok)
                                                .show()
                                    }, { error ->
                                        Log.d(TAG, "error occured joining room", error)
                                        MaterialDialog.Builder(this@MainActivity)
                                                .title(R.string.label_error)
                                                .content("Error joining $input")
                                                .positiveText(R.string.label_ok)
                                                .show()
                                    })
                        })
                        .show()
            }
        }
        return false
    }
}
