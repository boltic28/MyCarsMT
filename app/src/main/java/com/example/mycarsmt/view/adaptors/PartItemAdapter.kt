package com.example.mycarsmt.view.adaptors

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Part

class PartItemAdapter(partsIn: List<Part>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<PartItemAdapter.PartHolder>() {

    private var parts = partsIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PartHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false))

    override fun getItemCount() = parts.size

    override fun onBindViewHolder(holder: PartHolder, position: Int) = holder.bind(parts[position])


    inner class PartHolder(private val partRow: View) : RecyclerView.ViewHolder(partRow) {

        private val description = partRow.findViewById<TextView>(R.id.noteItemDescription)
        private val image = partRow.findViewById<ImageView>(R.id.noteItemImage)

        @SuppressLint("SetTextI18n")
        fun bind(part: Part) {

            description.text = part.description
            // image

            partRow.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(parts[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(part: Part)
    }
}