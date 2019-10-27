package com.example.xkcdcomicview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.LinearLayout
import java.io.ByteArrayInputStream

class XKCDRecyclerAdapter(private val XKCDComics: ArrayList<XKCDMinimalSaveToFile>, private val context: Context) :
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

    override fun onBindViewHolder(holder: XKCDViewHolder, position: Int) {
        holder.xkcdFavoriteTitle.text = XKCDComics[position].title.replace("_", " ")
        holder.xkcdFavoriteComicImage.setImageBitmap(byteArrayToBitmap(XKCDComics[position].imagebytearray))
        holder.xkcdFavoriteDescription.text = XKCDComics[position].description
        holder.xkcdFavoriteLayout.setOnClickListener{
            val intent = Intent(context, ViewFavoriteComic::class.java)
            intent.putExtra("title", XKCDComics[position].title)
            intent.putExtra("description", XKCDComics[position].description)
            intent.putExtra("explanation", XKCDComics[position].explanation)
            intent.putExtra("image", XKCDComics[position].imagebytearray)
            context.startActivity(intent)
        }
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
        var xkcdFavoriteLayout : LinearLayout = view.findViewById(R.id.xkcdFavoriteLayout)
    }
}