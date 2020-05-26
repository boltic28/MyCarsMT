package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.repo.note.NoteServiceImpl
import kotlinx.android.synthetic.main.fragment_creator_note.*
import java.time.LocalDate

class NoteCreator (contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "partCreator"

        fun getInstance(note: Note): CarCreator {

            val bundle = Bundle()
            bundle.putSerializable("note", note)

            val fragment = CarCreator(R.layout.fragment_creator_part)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var noteService: NoteServiceImpl
    private lateinit var note: Note
    private lateinit var handler: Handler
    private var isExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteService = NoteServiceImpl(view.context, handler)
        note = arguments?.getSerializable("note") as Note
        isExist = note.id > 0
        noteCreatorButtonDone.isActivated = false

        if (isExist) setCreatorData()

//buttons
        noteCreatorButtonCreate.setOnClickListener {

            note.description = noteCreatorDescription.text.toString()
//            important level set formatter

            if (isExist) {
                noteService.update(note)
            } else {
                note.date = LocalDate.now()
                noteService.create(note)
            }
        }
        noteCreatorButtonDone.setOnClickListener {
            //show fragmentDialog
            noteService.delete(note)
        }
        noteCreatorButtonCancel.setOnClickListener {
            onDestroy()
        }
    }

    private fun setCreatorData() {
        noteCreatorDescription.setText(note.description)

        noteCreatorButtonCreate.setText(R.string.update)
        noteCreatorButtonDone.isActivated = true
    }

}