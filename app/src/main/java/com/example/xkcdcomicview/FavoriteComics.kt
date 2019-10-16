package com.example.xkcdcomicview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteComics : AppCompatActivity() {

    private val listOfComics = ArrayList<XKCDDataClass>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_comics)


        viewManager = LinearLayoutManager(this)
        viewAdapter = XKCDRecyclerAdapter(listOfComics)
        recyclerView = findViewById<RecyclerView>(R.id.xkcdRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //listOfComics.add(testModel)
    }
}
