package com.example.xkcdcomicview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    /**
     * ERROR_TAG - Tag that will be used for Main Activity Errors in Log.e for debugging
     */
    private val ERROR_TAG = "Main_Activity_Error"

    /**
     * xkcdControlComicURL - URL Of the latest comic, this will never change and will always return
     * the latest ID (num) of comics; We will use that ID later to control the max value of what
     * we can request in the API
     */
    private val xkcdControlComicURL = "https://xkcd.com/info.0.json"

    /**
     * Creating GSON (Google' JSON interface) object to use for parsing the JSON
     */
    private val gson = Gson()

    lateinit var queue : RequestQueue

    /**
     * xkcdCurrentMaxNumber - ID Of latest release, this ID will remain same until new comic is released
     * xkcdComicNumber - ID Of comic we're on right now, this ID will change throughout the app use
     */
    private var xkcdCurrentMaxNumber: Int = 0
    private var xkcdComicNumber: Int = 0
    private var url : String = ""

    /**
     * xkcdNextComic - Queries API for comic with ID that is larger by current ID by 1
     * xkcdPreviousComic - Queries API for comic with ID that is smaller by current ID by 1
     * xkcdRandomComic - Queries API for comic with random ID from 1 to current highest
     * xkcdFirstComic - Queries API for the first comic with ID 1
     * xkcdLatestComic - Queries API for the latest comic with no id (By default returns latest one)
     * xkcdComicView - ImageView that will display the comic in Main Activity
     */
    lateinit var xkcdNextComic: Button
    lateinit var xkcdPreviousComic: Button
    lateinit var xkcdRandomComic: Button
    lateinit var xkcdFirstComic: Button
    lateinit var xkcdLatestComic: Button
    lateinit var xkcdComicView: ImageView


    private fun initComponents() {
        xkcdNextComic = findViewById(R.id.xkcdNextComic)
        xkcdPreviousComic = findViewById(R.id.xkcdPreviousComic)
        xkcdRandomComic = findViewById(R.id.xkcdRandomComic)
        xkcdFirstComic  = findViewById(R.id.xkcdFirstComic)
        xkcdLatestComic = findViewById(R.id.xkcdLatestComic)
        xkcdComicView = findViewById(R.id.xkcdComicView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()

        queue = Volley.newRequestQueue(this)
        val controlRequest = StringRequest(Request.Method.GET,
            xkcdControlComicURL,
            Response.Listener { response ->
                val controlModel = gson.fromJson(response, XKCDDataClass::class.java)
                xkcdCurrentMaxNumber = controlModel.num
                xkcdComicNumber = controlModel.num
                Picasso.get().load(controlModel.img).into(xkcdComicView)
            },
            Response.ErrorListener {
                Log.e(
                    ERROR_TAG,
                    "Trouble Submitting Request for Control URL"
                )
            })
        queue.add(controlRequest)

        xkcdNextComic.setOnClickListener {
            if (xkcdComicNumber == xkcdCurrentMaxNumber) {
                xkcdComicNumber = 0
            }
            xkcdComicNumber++
            url = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(url)
        }

        xkcdPreviousComic.setOnClickListener{
            if (xkcdComicNumber == 1) {
                xkcdComicNumber = xkcdCurrentMaxNumber + 1
            }
            xkcdComicNumber--
            url = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(url)
        }

        xkcdRandomComic.setOnClickListener{
            xkcdComicNumber = Random(System.currentTimeMillis()).nextInt(1, xkcdCurrentMaxNumber)
            url = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(url)
        }

        xkcdFirstComic.setOnClickListener{
            xkcdComicNumber = 1
            url = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(url)
        }
        xkcdLatestComic.setOnClickListener{
            xkcdComicNumber = xkcdCurrentMaxNumber
            url = "https://xkcd.com/${xkcdCurrentMaxNumber}/info.0.json"
            xkcdQueryComic(xkcdControlComicURL)
        }
    }

    private fun xkcdQueryComic(url : String){
        val nextRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                val nextComic = gson.fromJson(response, XKCDDataClass::class.java)
                Picasso.get().load(nextComic.img).into(xkcdComicView)
            },
            Response.ErrorListener { Log.e(ERROR_TAG, "Error Making Request") })
        queue.add(nextRequest)
    }
}


/*
- browse through the comics -------------------------------------- X
- see the comic details, including its description,
- search for comics by the comic number as well as text,
- get the comic explanation
- favorite the comics, which would be available offline too,
- send comics to others,
- get notifications when a new comic is published,
- support multiple form factors.
 */