package com.example.mycarsmt.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.domain.DiagnosticElement

class DiagnosticElementAdapter(
    diagnosticList: List<DiagnosticElement>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<DiagnosticElementAdapter.ElementHolder>() {

    private var elements = diagnosticList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ElementHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_diagnostic,
                parent,
                false
            )
        )

    override fun getItemCount() = elements.size

    override fun onBindViewHolder(holder: ElementHolder, position: Int) =
        holder.bind(elements[position])

    inner class ElementHolder(private val diagnosticRow: View) :
        RecyclerView.ViewHolder(diagnosticRow) {

        private val description = diagnosticRow.findViewById<TextView>(R.id.diagnosticItemData)
        private val car = diagnosticRow.findViewById<TextView>(R.id.diagnosticItemCar)

        @SuppressLint("SetTextI18n")
        fun bind(element: DiagnosticElement) {

            car.text = element.car.getFullName()

            val line = StringBuilder()
            element.list.forEach { if (it.isNotEmpty()) line.append(diagnosticRow.resources.getString(R.string.diagnostic_item_description, it) + "\n") }

            description.text = line.toString().dropLast(1)

            diagnosticRow.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(elements[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(element: DiagnosticElement)
    }
}