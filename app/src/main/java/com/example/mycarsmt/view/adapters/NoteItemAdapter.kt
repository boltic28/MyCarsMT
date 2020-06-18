package com.example.mycarsmt.view.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.enums.NoteLevel

class NoteItemAdapter(
    notesIn: List<Note>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<NoteItemAdapter.NoteHolder>() {

    private var notes = notesIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteHolder, position: Int) = holder.bind(notes[position])

    inner class NoteHolder(private val noteRow: View) : RecyclerView.ViewHolder(noteRow) {

        private val description = noteRow.findViewById<TextView>(R.id.diagnosticItemCar)
        private val image = noteRow.findViewById<ImageView>(R.id.noteItemImage)

        @SuppressLint("SetTextI18n")
        fun bind(note: Note) {

            description.text = note.description
            image.setImageResource(R.drawable.ic_attention)
            when(note.importantLevel){
                NoteLevel.INFO -> image.setColorFilter(Color.argb(255, 5, 180, 5))
                NoteLevel.LOW -> image.setColorFilter(Color.argb(255, 180, 180, 5))
                NoteLevel.MIDDLE -> image.setColorFilter(Color.argb(255, 230, 120, 5))
                NoteLevel.HIGH -> image.setColorFilter(Color.argb(255, 230, 5, 5))
            }

            noteRow.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(notes[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(note: Note)
    }
}