package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.enums.NoteLevel
import com.example.mycarsmt.model.repo.note.NoteServiceImpl
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.fragments.CarFragment
import com.example.mycarsmt.view.fragments.PartFragment
import kotlinx.android.synthetic.main.fragment_creator_note.*
import java.time.LocalDate

class NoteCreator (contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "noteCreator"

        fun getInstance(note: Note): NoteCreator {

            val bundle = Bundle()
            bundle.putSerializable("note", note)

            val fragment = NoteCreator(R.layout.fragment_creator_note)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var manager: MainActivity
    private lateinit var noteService: NoteServiceImpl
    private lateinit var note: Note
    private lateinit var handler: Handler
    private var isExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentManager()
        handler = Handler(view.context.mainLooper)
        noteService = NoteServiceImpl(view.context, handler)
        note = arguments?.getSerializable("note") as Note
        manager.title =
            if (note.id == 0L) "Create new car"
            else "Updating note"

        isExist = note.id > 0
        noteCreatorButtonDone.isActivated = false
        if (isExist) setCreatorData()

        noteCreatorButtonCreate.setOnClickListener {
            if(noteCreatorDescription.text.isNotEmpty()) {
                note.description = noteCreatorDescription.text.toString()

                if (isExist) {
                    noteService.update(note)
                } else {
                    note.date = LocalDate.now()
                    noteService.create(note)
                }
            }
            manager.loadPreviousFragment()
        }

        noteCreatorButtonDone.setOnClickListener {
            noteService.addRepair(note.done())
            noteService.delete(note)
            manager.loadPreviousFragment()
        }

        partPanelButtonCancel.setOnClickListener {
            manager.loadPreviousFragment()
        }

        noteCreatorLowImp.setOnClickListener {
            switchLowImptOfNote()
            note.importantLevel = NoteLevel.LOW
        }

        noteCreatorMidImpt.setOnClickListener {
            switchMiddleImptOfNote()
            note.importantLevel = NoteLevel.MIDDLE
        }

        noteCreatorHighImpt.setOnClickListener {
            switchHighImptOfNote()
            note.importantLevel = NoteLevel.HIGH
        }

        noteCreatorInfoImpt.setOnClickListener {
            switchInfoImptOfNote()
            note.importantLevel = NoteLevel.INFO
        }
    }

    private fun setCreatorData() {
        noteCreatorDescription.setText(note.description)

        when (note.importantLevel) {
            NoteLevel.LOW -> switchLowImptOfNote()
            NoteLevel.MIDDLE -> switchMiddleImptOfNote()
            NoteLevel.HIGH -> switchHighImptOfNote()
            NoteLevel.INFO -> switchInfoImptOfNote()
        }

        noteCreatorButtonCreate.setText(R.string.update)
        noteCreatorButtonDone.isActivated = true
    }

    private fun switchLowImptOfNote(){
        noteCreatorLowImp.isChecked = true
        noteCreatorMidImpt.isChecked = false
        noteCreatorHighImpt.isChecked = false
        noteCreatorInfoImpt.isChecked = false
    }

    private fun switchMiddleImptOfNote(){
        noteCreatorLowImp.isChecked = false
        noteCreatorMidImpt.isChecked = true
        noteCreatorHighImpt.isChecked = false
        noteCreatorInfoImpt.isChecked = false
    }

    private fun switchHighImptOfNote(){
        noteCreatorLowImp.isChecked = false
        noteCreatorMidImpt.isChecked = false
        noteCreatorHighImpt.isChecked = true
        noteCreatorInfoImpt.isChecked = false
    }

    private fun switchInfoImptOfNote(){
        noteCreatorLowImp.isChecked = false
        noteCreatorMidImpt.isChecked = false
        noteCreatorHighImpt.isChecked = false
        noteCreatorInfoImpt.isChecked = true
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity) {
            manager = mainActivity
        }
    }
}