package com.example.xkcdcomicview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val randomStrings = ArrayList<String>()
        randomStrings.add("one")
        randomStrings.add("two")
        randomStrings.add("three")
        viewManager = LinearLayoutManager(this)
        viewAdapter = XKCDRecyclerAdapter(randomStrings)

        recyclerView = findViewById<RecyclerView>(R.id.xkcdRecyclerView).apply {

            setHasFixedSize(true)

            layoutManager = viewManager
            adapter = viewAdapter

        }
    }
}
/*
- browse through the comics,
- see the comic details, including its description,
- search for comics by the comic number as well as text,
- get the comic explanation
- favorite the comics, which would be available offline too,
- send comics to others,
- get notifications when a new comic is published,
- support multiple form factors.
 */