package com.example.mycarsmt.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.NO_PHOTO
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.domain.Part
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
        private val serviceIcon = partRow.findViewById<ImageView>(R.id.partItemIconAttention)

        fun bind(part: Part) {

            name.text = part.name
            serviceData.text = partRow.context.resources.getString(
                R.string.part_to_change_with_result,
                part.getInfoToChange()
            )

            if (part.photo == NO_PHOTO || part.photo.isEmpty()) {
                Picasso.get().load(R.drawable.nophoto).into(image)
            } else {
                Picasso.get()
                    .load(
                        File(
                            Directories.PART_IMAGE_DIRECTORY.value,
                            partRow.resources.getString(R.string.photo_path, part.photo)
                        )
                    )
                    .into(image)
            }

            if (part.condition.contains(Condition.OK)) {
                serviceIcon.setImageResource(R.drawable.ic_ok)
                serviceIcon.setColorFilter(Color.argb(255, 5, 180, 5))
            }
            if (part.condition.contains(Condition.MAKE_INSPECTION)) {
                serviceIcon.setImageResource(R.drawable.ic_info)
                serviceIcon.setColorFilter(Color.argb(255, 10, 120, 5))
            }
            if (part.condition.contains(Condition.BUY_PARTS)) {
                serviceIcon.setImageResource(R.drawable.ic_buy)
                serviceIcon.setColorFilter(Color.argb(255, 180, 180, 5))
            }
            if (part.condition.contains(Condition.MAKE_SERVICE)) {
                serviceIcon.setImageResource(R.drawable.ic_service)
                serviceIcon.setColorFilter(Color.argb(255, 230, 120, 5))
            }
            if (part.condition.contains(Condition.OVERUSED)) {
                serviceIcon.setImageResource(R.drawable.ic_attention)
                serviceIcon.setColorFilter(Color.argb(255, 230, 5, 5))
            }

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