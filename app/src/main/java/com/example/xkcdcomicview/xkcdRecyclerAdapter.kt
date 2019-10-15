package com.example.xkcdcomicview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class XKCDRecyclerAdapter(private val XKCDComics: ArrayList<String>) :
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
        holder.testString.text = XKCDComics[position]
    }

    override fun getItemCount() = XKCDComics.size

    class XKCDViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var testString : TextView = view.findViewById(R.id.textView)

    }
}