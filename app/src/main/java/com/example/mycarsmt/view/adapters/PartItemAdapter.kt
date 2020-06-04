package com.example.mycarsmt.view.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.enums.Condition
import com.squareup.picasso.Picasso
import java.io.File

class PartItemAdapter(
    partsIn: List<Part>, private val listener: OnItemClickListener
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
            serviceData.text = part.getInfoToChange()

            if (part.photo == SpecialWords.NO_PHOTO.value || part.photo.isEmpty()) {
                Picasso.get().load(R.drawable.nophoto).into(image)
            } else {
                Picasso.get()
                    .load(File(Directories.PART_IMAGE_DIRECTORY.value, "${part.photo}.jpg"))
                    .into(image)
            }

            if (part.condition.contains(Condition.MAKE_INSPECTION))
                servIcon.setColorFilter(Color.argb(255, 10, 120, 5))
            if (part.condition.contains(Condition.BUY_PARTS))
                servIcon.setColorFilter(Color.argb(255, 180, 180, 5))
            if (part.condition.contains(Condition.MAKE_SERVICE))
                servIcon.setColorFilter(Color.argb(255, 230, 120, 5))
            if (part.condition.contains(Condition.ATTENTION))
                servIcon.setColorFilter(Color.argb(255, 230, 5, 5))

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