package com.example.mycarsmt.view.adaptors

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Note

class NoteItemAdapter(
    notesIn: List<Note>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<NoteItemAdapter.NoteHolder>() {

    private var notes = notesIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteHolder, position: Int) = holder.bind(notes[position])

    fun setNotes(list: List<Note>) {
        notes = list
        notifyDataSetChanged()
    }

    inner class NoteHolder(private val noteRow: View) : RecyclerView.ViewHolder(noteRow) {

        private val description = noteRow.findViewById<TextView>(R.id.noteItemDescription)
        private val image = noteRow.findViewById<ImageView>(R.id.noteItemImage)

        @SuppressLint("SetTextI18n")
        fun bind(note: Note) {

            description.text = note.description
            // image

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