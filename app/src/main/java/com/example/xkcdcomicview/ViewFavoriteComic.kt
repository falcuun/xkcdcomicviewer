package com.example.xkcdcomicview

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.io.ByteArrayInputStream

class ViewFavoriteComic : AppCompatActivity() {


    lateinit var xkcdFavoriteViewTitleView : TextView
    lateinit var xkcdFavoriteViewComicView : ImageView
    lateinit var xkcdFavoriteViewDescriptionView : TextView
    lateinit var xkcdFavoriteViewExplanationView : TextView

    private fun initCompoenents(){
        xkcdFavoriteViewTitleView = findViewById(R.id.xkcdFavoriteViewTitleView)
        xkcdFavoriteViewComicView = findViewById(R.id.xkcdFavoriteViewComicView)
        xkcdFavoriteViewDescriptionView = findViewById(R.id.xkcdFavoriteViewDescriptionView)
        xkcdFavoriteViewExplanationView = findViewById(R.id.xkcdFavoriteViewExplanationView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_favorite_comic)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val image = intent.getByteArrayExtra("image")
        val explanation = intent.getStringExtra("explanation")
        initCompoenents()

        val arrayInputStream = ByteArrayInputStream(image)
        xkcdFavoriteViewTitleView.text = title
        xkcdFavoriteViewComicView.setImageBitmap(BitmapFactory.decodeStream(arrayInputStream))
        xkcdFavoriteViewDescriptionView.text = description
        xkcdFavoriteViewExplanationView.text = explanation
    }
}
