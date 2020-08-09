package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.NoteLevel
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import kotlinx.android.synthetic.main.fragment_creator_note.*
import java.time.LocalDate
import javax.inject.Inject

class NoteCreator @Inject constructor () : Fragment(R.layout.fragment_creator_note) {

    private

    companion object {
        const val FRAG_TAG = "noteCreator"
        const val TAG = "testmt"
    }

    @Inject
    lateinit var model: NoteCreatorModel

    private lateinit var note: Note
    private lateinit var car: Car

    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        note = arguments?.getSerializable("note") as Note
        isExist = note.id > 0
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // solve how can return back to car or main view.

        loadCar()

        setTitle()
        noteCreatorButtonDone.isActivated = false
        if (isExist) setCreatorData()

        noteCreatorButtonCreate.setOnClickListener {
            if(noteCreatorDescription.text.isNotEmpty()) {
                note.description = noteCreatorDescription.text.toString()

                if (isExist) {
                    model.noteService.update(note)
                } else {
                    note.date = LocalDate.now()
                    model.noteService.create(note)
                }
            }
            val bundle = Bundle()
            bundle.putSerializable("car", car)
            view.findNavController().navigate(R.id.action_noteCreator_to_carFragment, bundle)
        }

        noteCreatorButtonDone.setOnClickListener {
            model.noteService.addRepair(note.done())
            model.noteService.delete(note)

            val bundle = Bundle()
            bundle.putSerializable("car", car)
            view.findNavController().navigate(R.id.action_noteCreator_to_carFragment, bundle)
        }

        partPanelButtonCancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("car", car)
            view.findNavController().navigate(R.id.action_noteCreator_to_carFragment, bundle)
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

    @SuppressLint("CheckResult")
    private fun loadCar(){
        model.carService.readById(note.carId).subscribe { car = it }
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

    private fun setTitle(){
        activity?.title =
            if (note.id == 0L) "Create new car"
            else "Updating note"
    }
}