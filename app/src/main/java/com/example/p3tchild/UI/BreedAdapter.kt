package com.example.p3tchild.UI

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.p3tchild.Model.Breed
import com.example.p3tchild.R

class BreedAdapter(private val context: Context,
                   private val dataSource: List<Breed>): BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_breed, parent, false)
        val name = rowView.findViewById<TextView>(R.id.breedTextView)
        val score = rowView.findViewById<TextView>(R.id.scoreTextView)

        val breed = getItem(position) as Breed
        name.text = breed.Name
        score.text = breed.Score.toString()

        return rowView
    }

}