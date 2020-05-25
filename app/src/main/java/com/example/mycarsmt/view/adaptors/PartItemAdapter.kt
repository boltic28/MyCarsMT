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
import java.time.LocalDate

class PartItemAdapter(partsIn: List<Part>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<PartItemAdapter.PartHolder>() {

    private var parts = partsIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PartHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false))

    override fun getItemCount() = parts.size

    override fun onBindViewHolder(holder: PartHolder, position: Int) = holder.bind(parts[position])


    inner class PartHolder(private val partRow: View) : RecyclerView.ViewHolder(partRow) {

        private val name = partRow.findViewById<TextView>(R.id.partItemName)
        private val serviceData = partRow.findViewById<TextView>(R.id.partItemToServiceInfo)
        private val image = partRow.findViewById<ImageView>(R.id.partItemImage)
        private val servIcon = partRow.findViewById<ImageView>(R.id.partItemIconAttention)

        @SuppressLint("SetTextI18n")
        fun bind(part: Part) {

            name.text = part.name
            serviceData.text = "to service: ${part.dateLastChange} km/${part.mileage} days"
            // image
            //icon

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