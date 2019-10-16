package com.example.xkcdcomicview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayInputStream


class XKCDRecyclerAdapter(private val XKCDComics: ArrayList<XKCDMinimalSaveToFile>) :
            RecyclerView.Adapter<XKCDRecyclerAdapter.XKCDViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): XKCDViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(
            R.layout.test_recycler_item,
            parent,
            false
        )

        return XKCDViewHolder(textView)
    }
    // TODO RecyclerView 1: Make Initial Fav Display show only short Description, on Click, show full
    override fun onBindViewHolder(holder: XKCDViewHolder, position: Int) {
        holder.xkcdFavoriteTitle.text = XKCDComics[position].title.replace("_", " ")
        holder.xkcdFavoriteComicImage.setImageBitmap(byteArrayToBitmap(XKCDComics[position].imagebytearray))
        holder.xkcdFavoriteDescription.text = XKCDComics[position].description
        holder.xkcdFavoriteExplanation.text = XKCDComics[position].explanation
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }

    override fun getItemCount() = XKCDComics.size

    class XKCDViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var xkcdFavoriteTitle : TextView = view.findViewById(R.id.xkcdFavoriteTitle)
        var xkcdFavoriteComicImage : ImageView = view.findViewById(R.id.xkcdFavoriteComicImage)
        var xkcdFavoriteDescription : TextView = view.findViewById(R.id.xkcdFavoriteDescription)
        var xkcdFavoriteExplanation : TextView = view.findViewById(R.id.xkcdFavoriteExplanation)

    }
}