package com.example.xkcdcomicview

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.random.Random
import android.widget.*
import androidx.core.text.isDigitsOnly
import android.widget.Toast
import android.os.Handler
import java.util.regex.Pattern
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    /**
     * ERROR_TAG - Tag that will be used for Main Activity Errors in Log.e for debugging
     * OPEN_EXPLANATION_ACTIVITY_REQUEST_CODE - Code that is used to Open explanation activity
     * GRANT_STORAGE_PERMISSION_CODE - Code that will be used for requesting Storage permission
     */
    private val ERROR_TAG = "Main_Activity_Error"
    private val OPEN_EXPLANATION_ACTIVITY_REQUEST_CODE = 1
    private val GRANT_STORAGE_PERMISSION_CODE = 1

    companion object {
        val EXPLANATION_TEXT_INTENT_TAG = "explanation_text"
    }


    /**
     * xkcdControlComicxkcdURL - xkcdURL Of the latest comic, this will never change and will always return
     * the latest ID (num) of comics; We will use that ID later to control the max value of what
     * we can request in the API
     */
    private val xkcdControlComicxkcdURL = "https://xkcd.com/info.0.json"

    /**
     * Creating GSON (Google' JSON interface) object to use for parsing the JSON
     */
    private val gson = Gson()

    /**
     * queue - RequestQueue that will be used to send requests to the API
     * xkcdCurrentComic - Data class that is used to store comic data
     * xkcdExplanationString - A damn long string that will be the Explanation
     */
    lateinit var queue: RequestQueue
    lateinit var xkcdCurrentComic: XKCDDataClass
    lateinit var xkcdExplanationString: String


    /**
     * xkcdCurrentMaxNumber - ID Of latest release, this ID will remain same until new comic is released
     * xkcdComicNumber - ID Of comic we're on right now, this ID will change throughout the app use
     * xkcdURL - URL Used to Query the API for the JSON string of the Comic
     * xkcdExplanationxkcdURL - URL Used to Query WikiMedia API to get the Explanation for each comic
     * exit - Flag used to control onBackPressed for closing the application
     */
    private var xkcdCurrentMaxNumber: Int = 0
    private var xkcdComicNumber: Int = 0
    private var xkcdURL: String = ""
    private var xkcdExplanationxkcdURL: String = ""
    private var exit = false


    /**
     * xkcdNextComic - Queries API for comic with ID that is larger by current ID by 1
     * xkcdPreviousComic - Queries API for comic with ID that is smaller by current ID by 1
     * xkcdRandomComic - Queries API for comic with random ID from 1 to current highest
     * xkcdFirstComic - Queries API for the first comic with ID 1
     * xkcdLatestComic - Queries API for the latest comic with no id (By default returns latest one)
     * xkcdComicView - ImageView that will display the comic in Main Activity
     * xkcdTitleView - TextView that will display the comic title above the comic itself
     * xkcdDescriptionView - TextView that will display the comic description
     * xkcdOpenExplanation - ImageButton that opens the new Activity Displaying the Explanation of the comic
     * xkcdFavoriteButton - ImageButton that will add the current comic to the favorites for offline viewing
     * xkcdOpenFavoriteComics - ImageButton that will open the 'library' with all the favorite comics
     * xkcdShareComic - ImageButton that will open the share intent that will copy the link and feed it to other applications
     */
    lateinit var xkcdNextComic: Button
    lateinit var xkcdPreviousComic: Button
    lateinit var xkcdRandomComic: Button
    lateinit var xkcdFirstComic: Button
    lateinit var xkcdLatestComic: Button
    lateinit var xkcdComicView: ImageView
    lateinit var xkcdTitleView: TextView
    lateinit var xkcdDescriptionView: TextView
    lateinit var xkcdOpenExplanation: ImageButton
    lateinit var xkcdFavoriteButton: ImageButton
    lateinit var xkcdOpenFavoriteComics: ImageButton
    lateinit var xkcdShareComic: ImageButton


    private fun initComponents() {
        xkcdNextComic = findViewById(R.id.xkcdNextComic)
        xkcdPreviousComic = findViewById(R.id.xkcdPreviousComic)
        xkcdRandomComic = findViewById(R.id.xkcdRandomComic)
        xkcdFirstComic = findViewById(R.id.xkcdFirstComic)
        xkcdLatestComic = findViewById(R.id.xkcdLatestComic)
        xkcdComicView = findViewById(R.id.xkcdComicView)
        xkcdTitleView = findViewById(R.id.xkcdTitleView)
        xkcdDescriptionView = findViewById(R.id.xkcdDescriptionView)
        xkcdOpenExplanation = findViewById(R.id.xkcdOpenExplanation)
        xkcdFavoriteButton = findViewById(R.id.xkcdFavoriteButton)
        xkcdOpenFavoriteComics = findViewById(R.id.xkcdOpenFavoriteComics)
        xkcdShareComic = findViewById(R.id.xkcdShareComic)

        queue = Volley.newRequestQueue(this)
    }

    private fun requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GRANT_STORAGE_PERMISSION_CODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        requestStoragePermission()

        xkcdOpenFavoriteComics.setOnClickListener{
            val favoriteComics = Intent(this@MainActivity, FavoriteComics::class.java)
            startActivity(favoriteComics)
        }

        xkcdQueryComic(xkcdControlComicxkcdURL)
        val controlRequest = StringRequest(Request.Method.GET,
            xkcdControlComicxkcdURL,
            Response.Listener { response ->
                xkcdCurrentComic = gson.fromJson(response, XKCDDataClass::class.java)
                xkcdCurrentMaxNumber = xkcdCurrentComic.num
                xkcdComicNumber = xkcdCurrentComic.num
                Picasso.get().load(xkcdCurrentComic.img).into(xkcdComicView)
                xkcdTitleView.text = xkcdCurrentComic.title
                xkcdDescriptionView.text = xkcdCurrentComic.alt
                xkcdExplanationxkcdURL =
                    "https://www.explainxkcd.com/wiki/api.php?action=parse&page=${xkcdCurrentComic.num}:_${xkcdCurrentComic.title.replace(
                        " ",
                        "_"
                    )}&prop=wikitext&sectiontitle=Explanation&format=json"
                assignButtonListeners()
            },
            Response.ErrorListener {
                Log.e(
                    ERROR_TAG,
                    "Trouble Submitting Request for Control xkcdURL"
                )
            })
        queue.add(controlRequest)
    }

    private fun assignButtonListeners() {
        xkcdNextComic.setOnClickListener {
            if (xkcdComicNumber == xkcdCurrentMaxNumber) {
                xkcdComicNumber = 0
            }
            xkcdComicNumber++
            xkcdURL = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(xkcdURL)
        }

        xkcdPreviousComic.setOnClickListener {
            if (xkcdComicNumber == 1) {
                xkcdComicNumber = xkcdCurrentMaxNumber + 1
            }
            xkcdComicNumber--
            xkcdURL = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(xkcdURL)
        }

        xkcdRandomComic.setOnClickListener {
            xkcdComicNumber = Random(System.currentTimeMillis()).nextInt(1, xkcdCurrentMaxNumber)
            xkcdURL = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(xkcdURL)
        }

        xkcdFirstComic.setOnClickListener {
            xkcdComicNumber = 1
            xkcdURL = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
            xkcdQueryComic(xkcdURL)
        }
        xkcdLatestComic.setOnClickListener {
            xkcdComicNumber = xkcdCurrentMaxNumber
            xkcdURL = "https://xkcd.com/${xkcdCurrentMaxNumber}/info.0.json"
            xkcdQueryComic(xkcdControlComicxkcdURL)
        }

        xkcdOpenExplanation.setOnClickListener {
            xkcdGetExplanation(xkcdExplanationxkcdURL, OPEN_EXPLANATION_ACTIVITY_REQUEST_CODE)
        }

        xkcdFavoriteButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Comic Successfully saved!", Toast.LENGTH_LONG).show()
                xkcdSaveFavoriteComic(xkcdCurrentComic)
            } else {
                Toast.makeText(this@MainActivity, "You need to grant storage Permission to do that", Toast.LENGTH_LONG).show()
                requestStoragePermission()
            }
        }
    }

    private fun xkcdQueryComic(xkcdURL: String?) {
        val nextRequest = StringRequest(Request.Method.GET, xkcdURL,
            Response.Listener { response ->
                xkcdCurrentComic = gson.fromJson(response, XKCDDataClass::class.java)
                xkcdExplanationxkcdURL =
                    "https://www.explainxkcd.com/wiki/api.php?action=parse&page=${xkcdCurrentComic.num}:_${xkcdCurrentComic.title.replace(
                        " ",
                        "_"
                    )}&prop=wikitext&sectiontitle=Explanation&format=json"
                Picasso.get().load(xkcdCurrentComic.img).into(xkcdComicView)
                xkcdTitleView.text = xkcdCurrentComic.title
                xkcdDescriptionView.text = xkcdCurrentComic.alt
                xkcdGetExplanation(xkcdExplanationxkcdURL, 2)

                xkcdShareComic.setOnClickListener{
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://xkcd.com/${xkcdCurrentComic.num}")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            },
            Response.ErrorListener {
                when (it.networkResponse.statusCode) {
                    404 -> {
                        Picasso.get()
                            .load("https://st2.depositphotos.com/7023650/9863/v/950/depositphotos_98631012-stock-illustration-404-error-page-not-found.jpg")
                            .into(xkcdComicView)
                        xkcdTitleView.text = getString(R.string.four_o_four)
                        xkcdDescriptionView.text = getString(R.string.four_o_four)
                    }
                }
                Log.e(ERROR_TAG, "Error Making Request")
            })
        queue.add(nextRequest)
    }

    private fun xkcdGetExplanation(explainxkcdURL: String?, requestCode: Int) {
        val nextRequest = StringRequest(Request.Method.GET, explainxkcdURL,
            Response.Listener { response ->
                if(response.contains("==Explanation==")){
                xkcdExplanationString = response
                    .substring(
                        response.indexOf("==Explanation==") + 2,
                        response.indexOf("==Transcript==")
                    )
                    .replace("\\n", "\n")
                    .replace("\\", "")
                    .replace(Regex(Pattern.quote("{[a-zA-Z][0-9][\\s]}")), " ")

                if (requestCode == OPEN_EXPLANATION_ACTIVITY_REQUEST_CODE) {
                    xkcdOpenExplanationActivity(xkcdExplanationString)
                }
                }else {
                    Toast.makeText(this@MainActivity, "Explanation Does not Yet exist for this comic", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { Log.e(ERROR_TAG, "Error Making Request") })
        queue.add(nextRequest)
    }

    private fun xkcdOpenExplanationActivity(explain: String) {
        val xkcdExplanationIntent = Intent(this, XKCDExplanationActivity::class.java)
        xkcdExplanationIntent.putExtra(EXPLANATION_TEXT_INTENT_TAG, explain)
        startActivity(xkcdExplanationIntent)
    }

    private fun xkcdSaveFavoriteComic(xkcdComic: XKCDDataClass) {
        val bitmap = (xkcdComicView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val xkcdImageByteArray = stream.toByteArray()
        val xkcdMin = XKCDMinimalSaveToFile(
            xkcdComic.title,
            xkcdComic.alt,
            xkcdExplanationString,
            xkcdImageByteArray
        )

        val filename = "${xkcdComic.title.replace(" ", "_")}.json"
        val fileContents =  gson.toJson(xkcdMin)
        openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            GRANT_STORAGE_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission to write to storage was denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchbar, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isDigitsOnly()) {
                        xkcdComicNumber = query.toInt()
                        xkcdURL = "https://xkcd.com/${xkcdComicNumber}/info.0.json"
                        xkcdQueryComic(xkcdURL)

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Currently Only numbers Supported",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    return true
                }
            })
        }
        return true
    }

    override fun onBackPressed() {
        if (exit) {
            finish()
        } else {
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
            Handler().postDelayed({ exit = false }, 3 * 1000)
        }
    }
}


/*
- browse through the comics -------------------------------------- X
- see the comic details, including its description --------------- X
- search for comics by the comic number as well as text ---------- x
- get the comic explanation -------------------------------------- X
- favorite the comics, which would be available offline too ------ X
- send comics to others ------------------------------------------ X
- get notifications when a new comic is published,
- support multiple form factors.
 */