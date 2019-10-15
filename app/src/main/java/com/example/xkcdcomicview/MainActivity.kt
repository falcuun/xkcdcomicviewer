package com.example.xkcdcomicview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val randomStrings = ArrayList<String>()

    private fun populateList(){
        randomStrings.add("one")
        randomStrings.add("two")
        randomStrings.add("three")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateList()

        val queue = Volley.newRequestQueue(this)
        val url = "https://xkcd.com/1000/info.0.json"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                var gson = Gson()
                var testModel = gson.fromJson(response, XKCDDataClass::class.java)

                Log.e("SPARTAAAA", testModel.img)
            },
            Response.ErrorListener { Log.e("TROJAAAAAA", "Cause Sparta FTW!") })


        queue.add(stringRequest)


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