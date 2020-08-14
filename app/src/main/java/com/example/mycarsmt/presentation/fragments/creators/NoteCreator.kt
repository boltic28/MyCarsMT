package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.NOTE
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.SpecialWords.Companion.TARGET
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.NoteLevel
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    private var car: Car? = null
    private var part: Part? = null

    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        note = arguments?.getSerializable(NOTE) as Note

        arguments?.containsKey(CAR)?.let { car = arguments?.getSerializable(CAR) as Car }
        arguments?.containsKey(PART)?.let { part = arguments?.getSerializable(PART) as Part }
        isExist = note.id > 0
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCar()
        setTitle()
        noteCreatorButtonDone.isActivated = false
        if (isExist) setCreatorData()

        noteCreatorButtonCreate.setOnClickListener {
            if(noteCreatorDescription.text.isNotEmpty()) {
                note.description = noteCreatorDescription.text.toString()

                if (isExist) {
                    createNote()
                } else {
                    updateNote()
                }
                loadPreviousFragment()
            }
        }

        noteCreatorButtonDone.setOnClickListener {
            model.noteService.addRepair(note.done())
            model.noteService.delete(note)

            loadFragmentAfterDone()
        }

        partPanelButtonCancel.setOnClickListener {
            view.findNavController().navigateUp()
//            val bundle = Bundle()
//            bundle.putSerializable(CAR, car)
//            view.findNavController().navigate(R.id.action_noteCreator_to_carFragment, bundle)
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
    private fun createNote(){
        model.noteService.create(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { id ->
                    note.id = id
                    loadPreviousFragment()
                },
                { err ->
                    Log.d(TAG, "NOTE CREATOR: $err")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun updateNote(){
        note.date = LocalDate.now()
        model.noteService.update(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { number ->
                    if(number > 0)
                    loadPreviousFragment()
                }, {
                    Log.d(TAG, "NOTE CREATOR: $it")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun loadCar(){
        model.carService.readById(note.carId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    car = it
                },{
                    Log.d(TAG, "NOTE CREATOR: $it")
                }
            )
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

    private fun loadFragmentAfterDone(){
        val bundle = Bundle()
        if (car != null) {
            bundle.putSerializable(CAR, car)
            view?.findNavController()?.navigate(R.id.action_noteCreator_to_carFragment, bundle)
        }else {
            bundle.putString(TARGET, NOTE)
            view?.findNavController()?.navigate(R.id.action_noteCreator_to_mainListFragment, bundle)
        }
    }

    private fun loadPreviousFragment(){
        val bundle = Bundle()
        if (car != null){
            bundle.putSerializable(CAR, car)
            view?.findNavController()?.navigate(R.id.action_noteCreator_to_carFragment, bundle)
        }
        if (part != null){
            bundle.putSerializable(PART, part)
            view?.findNavController()?.navigate(R.id.action_noteCreator_to_partFragment, bundle)
        }
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