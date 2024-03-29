package com.example.xkcdcomicview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import android.os.Environment
import java.io.*

class FavoriteComics : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val listOfComicNames = ArrayList<String>()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_comics)
        val listOfComics = ArrayList<XKCDMinimalSaveToFile>()
        listExternalStorage()

        listOfComicNames.forEach {
            val filename = it
            val fileInputStream: FileInputStream?
            fileInputStream = openFileInput(filename)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            val xkcdMin = gson.fromJson(stringBuilder.toString(), XKCDMinimalSaveToFile::class.java)
            listOfComics.add(xkcdMin)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = XKCDRecyclerAdapter(listOfComics, this@FavoriteComics)
        recyclerView = findViewById<RecyclerView>(R.id.xkcdRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
    private fun listExternalStorage() {
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            listFiles(File(this.filesDir.path))
        }
    }
    private fun listFiles(directory: File) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) {
                    if (file.isDirectory) {
                        listFiles(file)
                    } else {
                        listOfComicNames.add(file.name)
                    }
                }
            }
        }
    }
}
