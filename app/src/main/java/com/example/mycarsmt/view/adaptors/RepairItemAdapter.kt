package com.example.mycarsmt.view.adaptors

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Repair

class RepairItemAdapter(repairsIn: List<Repair>, private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<RepairItemAdapter.RepairHolder>() {

    private var repairs = repairsIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepairHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun getItemCount() = repairs.size

    override fun onBindViewHolder(holder: RepairHolder, position: Int) = holder.bind(repairs[position])

        inner class RepairHolder(private val repairRow: View) : RecyclerView.ViewHolder(repairRow) {

            private val description = repairRow.findViewById<TextView>(R.id.noteItemDescription)
            private val image = repairRow.findViewById<ImageView>(R.id.noteItemImage)

            @SuppressLint("SetTextI18n")
            fun bind(repair: Repair) {

                description.text = repair.description
                // image

                repairRow.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onClick(repairs[adapterPosition])
                    }
                }
            }
        }

    interface OnItemClickListener {
        fun onClick(repair: Repair)
    }
}