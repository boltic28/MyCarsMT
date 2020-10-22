package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.NOTE
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.enums.NoteLevel
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_creator_note.*
import java.time.LocalDate
import javax.inject.Inject

class NoteCreator @Inject constructor () : Fragment(R.layout.fragment_creator_note) {

    companion object {
        const val FRAG_TAG = "noteCreator"
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var model: NoteCreatorModel

    private lateinit var navController: NavController
    private lateinit var note: Note
    private var car: Car? = null
    private var part: Part? = null

    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        note = arguments?.getSerializable(NOTE) as Note
        isExist = note.id > 0
        loadOwners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        setTitle()
        noteCreatorButtonDone.isActivated = false
        if (isExist) setCreatorData()

        noteCreatorButtonCreate.setOnClickListener {
            if(noteCreatorDescription.text.isNotEmpty()) {
                note.description = noteCreatorDescription.text.toString()

                if (isExist) {
                    updateNote()
                } else {
                    createNote()
                }
                loadPreviousFragment()
            }else{
                showMessage(resources.getString(R.string.note_creator_message_bad_description))
            }
        }

        noteCreatorButtonDone.setOnClickListener {
            model.repairService.create(note.done())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.d(TAG, "NOTE CREATOR: done")
                    },{
                        Log.d(TAG, "NOTE CREATOR: $it")
                    }
                )
            model.noteService.delete(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        loadPreviousFragment()
                    },{err ->
                        Log.d(TAG, "NOTE CREATOR: $err")
                        loadPreviousFragment()
                    }
                )
        }

        partPanelButtonCancel.setOnClickListener {
            navController.navigateUp()
        }

        noteCreatorLowImp.setOnClickListener {
            switchLowImportantOfNote()
            note.importantLevel = NoteLevel.LOW
        }

        noteCreatorMidImpt.setOnClickListener {
            switchMiddleImportantOfNote()
            note.importantLevel = NoteLevel.MIDDLE
        }

        noteCreatorHighImpt.setOnClickListener {
            switchHighImportantOfNote()
            note.importantLevel = NoteLevel.HIGH
        }

        noteCreatorInfoImpt.setOnClickListener {
            switchInfoImportantOfNote()
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
                    Log.d(TAG, "NOTE CREATOR: updated $id owner ${car?.number}")
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
                {
                    Log.d(TAG, "NOTE CREATOR: updated $it")
                }, {
                    Log.d(TAG, "NOTE CREATOR: error $it")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun loadOwners(){
        model.carService.getById(note.carId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    car = it
                    note.mileage = it.mileage
                    setTitle()
                },{
                    Log.d(TAG, "NOTE CREATOR: $it")
                }
            )

        if (note.partId != 0L){
            model.partService.getById(note.partId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        part = it
                    },{
                        Log.d(TAG, "NOTE CREATOR: $it")
                    }
                )
        }else{
            part = Part()
        }
    }

    private fun setCreatorData() {
        noteCreatorDescription.setText(note.description)

        when (note.importantLevel) {
            NoteLevel.LOW -> switchLowImportantOfNote()
            NoteLevel.MIDDLE -> switchMiddleImportantOfNote()
            NoteLevel.HIGH -> switchHighImportantOfNote()
            NoteLevel.INFO -> switchInfoImportantOfNote()
        }

        noteCreatorButtonCreate.setText(R.string.update)
        noteCreatorButtonDone.isActivated = true
    }

    private fun loadPreviousFragment(){
        navController.navigateUp()
    }

    private fun switchLowImportantOfNote(){
        noteCreatorLowImp.isChecked = true
        noteCreatorMidImpt.isChecked = false
        noteCreatorHighImpt.isChecked = false
        noteCreatorInfoImpt.isChecked = false
    }

    private fun switchMiddleImportantOfNote(){
        noteCreatorLowImp.isChecked = false
        noteCreatorMidImpt.isChecked = true
        noteCreatorHighImpt.isChecked = false
        noteCreatorInfoImpt.isChecked = false
    }

    private fun switchHighImportantOfNote(){
        noteCreatorLowImp.isChecked = false
        noteCreatorMidImpt.isChecked = false
        noteCreatorHighImpt.isChecked = true
        noteCreatorInfoImpt.isChecked = false
    }

    private fun switchInfoImportantOfNote(){
        noteCreatorLowImp.isChecked = false
        noteCreatorMidImpt.isChecked = false
        noteCreatorHighImpt.isChecked = false
        noteCreatorInfoImpt.isChecked = true
    }

    private fun setTitle(){
        activity?.title =
            if (note.id == 0L) resources.getString(R.string.note_creator_create_new)
            else {
                resources.getString(R.string.note_creator_updating_for_car, car?.number)
            }
    }

    private fun showMessage(msg: String){
        view?.let {
            Snackbar.make(it,msg, Snackbar.LENGTH_SHORT).show()
        }
    }
}