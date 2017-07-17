package me.duncanleo.diporto.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import me.duncanleo.diporto.R
import me.duncanleo.diporto.adapter.RoomsRecyclerViewAdapter
import me.duncanleo.diporto.model.Room
import me.duncanleo.diporto.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fakeUser = User("John Smith", "john@gmail.com", "johnsmith")

        roomsRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        roomsRecyclerView.addItemDecoration(DividerItemDecoration(baseContext, DividerItemDecoration.VERTICAL))
        roomsRecyclerView.adapter = RoomsRecyclerViewAdapter(listOf(
                Room(0, "Some Room", fakeUser, listOf()),
                Room(1, "Some Other Room", fakeUser, listOf())
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
