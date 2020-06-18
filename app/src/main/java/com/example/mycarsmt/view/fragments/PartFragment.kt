package com.example.mycarsmt.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.enums.ContentType
import com.example.mycarsmt.model.repo.part.PartServiceImpl
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_NOTES_FOR_PART
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_PART_READ
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_PART_UPDATED
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_REPAIRS_FOR_PART
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.adapters.NoteItemAdapter
import com.example.mycarsmt.view.adapters.RepairItemAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_part.*
import java.io.File

class PartFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    companion object {
        const val FRAG_TAG = "partFragment"
        const val TAG = "testmt"

        fun getInstance(part: Part): PartFragment {

            val bundle = Bundle()
            bundle.putSerializable("part", part)

            val fragment = PartFragment(R.layout.fragment_part)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var manager: MainActivity
    private lateinit var partService: PartServiceImpl
    private lateinit var part: Part
    private lateinit var handler: Handler
    private var notes: List<Note> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = ContentType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentManager()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHandler()

        partService = PartServiceImpl(view.context, handler)

        part = arguments?.getSerializable("part") as Part
        partService.readById(part.id)

        partService.getNotesFor(part)
        partService.getRepairsFor(part)
        loadPartData()

        loadPhoto()
        setRecycler()
        setAdapter()

        partPanelButtonNotes.setOnClickListener {
            contentType = ContentType.NOTES
            setAdapter()
        }
        partPanelButtonButtonRepairs.setOnClickListener {
            contentType = ContentType.REPAIRS
            setAdapter()
        }
        partPanelButtonService.setOnClickListener {
            manager.loadServiceFragment(part)
            // snack about service
        }
        partPanelFABSet.setOnClickListener {
            contentType = ContentType.DEFAULT
            manager.loadPartCreator(part)
        }
        partPanelButtonCancel.setOnClickListener {
            if (contentType == ContentType.DEFAULT) {
                manager.loadPreviousFragment(this)
            } else {
                hideRecycler()
            }
        }
        partPanelRecyclerAddButton.setOnClickListener {
            when (contentType) {
                ContentType.NOTES -> {
                    val note = Note()
                    note.carId = part.carId
                    note.partId = part.id
                    note.mileage = part.mileage
                    manager.loadNoteCreator(note)
                }
                ContentType.REPAIRS -> {
                    val repair = Repair()
                    repair.carId = part.carId
                    repair.partId = part.id
                    repair.mileage = part.mileage
                    manager.loadRepairCreator(repair)
                }
                else -> {

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadPartData() {
        manager.title = part.name

        partCreatorTextName.text = part.name
        partPanelTextToChangeDKM.text = part.getInfoToChange()
        partPanelTextInstallAtKM.text = "${part.mileageLastChange} km"
        partPanelTextInstallAtDate.text = "${part.dateLastChange}"
        partPanelTextListOfCodes.text = part.codes
        partPanelTextDescriptionBody.text = part.description
    }

    @SuppressLint("SetTextI18n")
    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            when (msg.what) {
                RESULT_PART_UPDATED -> {
                    part = msg.obj as Part
                    loadPartData()
                    Log.d(
                        TAG,
                        "HandlerPF: took notes from database: list size ${part.name}"
                    )
                    true
                }
                RESULT_NOTES_FOR_PART -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "HandlerPF: took notes from database: list size ${notes.size}"
                    )
                    if (contentType == ContentType.NOTES) setAdapter()
                    true
                }
                RESULT_REPAIRS_FOR_PART -> {
                    repairs = msg.obj as List<Repair>
                    Log.d(
                        TAG,
                        "HandlerPF: took repairs from database: list size ${repairs.size}"
                    )
                    if (contentType == ContentType.REPAIRS) setAdapter()
                    true
                }
//                RESULT_PART_READ -> {
//                    part = msg.obj as Part
//                    loadPartData()
//                    Log.d(
//                        TAG,
//                        "HandlerPF: took part from database"
//                    )
//                    partService.getNotesFor(part)
//                    partService.getRepairsFor(part)
//                    true
//                }
                else -> {
                    false
                }
            }
        })
    }

    private fun setRecycler() {
        partPanelRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setAdapter() {
        when (contentType) {
            ContentType.DEFAULT -> {
                if (partPanelRecycler.visibility == View.VISIBLE) hideRecycler()
            }
            ContentType.NOTES -> {
                if (partPanelRecycler.visibility == View.GONE) showRecycler()
                checkOnEmpty(notes)
                partPanelRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        manager.loadNoteCreator(note)
                    }
                })
            }
            ContentType.REPAIRS -> {
                if (partPanelRecycler.visibility == View.GONE) showRecycler()
                checkOnEmpty(repairs)
                partPanelRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        manager.loadRepairCreator(repair)
                    }
                })
            }
            else -> {

            }
        }
    }

    private fun loadPhoto() {
        if (part.photo == SpecialWords.NO_PHOTO.value || part.photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(partPanelMainImage)
        } else {
            Picasso.get().load(File(Directories.PART_IMAGE_DIRECTORY.value, "${part.photo}.jpg"))
                .into(partPanelMainImage)
        }
    }

    private fun checkOnEmpty(list: List<*>) {
        if (list.isEmpty()) partPanelNoElements.visibility = View.VISIBLE
        else partPanelNoElements.visibility = View.GONE
    }

    private fun hideRecycler() {
        partPanelRecycler.visibility = View.GONE
        partPanelRecyclerAddButton.visibility = View.GONE

        partPanelTextInstalledInfo.visibility = View.VISIBLE
        partPanelTextInstallAtDate.visibility = View.VISIBLE
        partPanelTextInstallAtKM.visibility = View.VISIBLE
        partPanelTextDescription.visibility = View.VISIBLE
        partPanelTextDescriptionBody.visibility = View.VISIBLE
        partPanelTextListOfCodes.visibility = View.VISIBLE
        partPanelTextCodesAndCross.visibility = View.VISIBLE

        partPanelNoElements.visibility = View.GONE
        contentType = ContentType.DEFAULT
    }

    private fun showRecycler() {
        partPanelTextInstalledInfo.visibility = View.GONE
        partPanelTextInstallAtDate.visibility = View.GONE
        partPanelTextInstallAtKM.visibility = View.GONE
        partPanelTextDescription.visibility = View.GONE
        partPanelTextDescriptionBody.visibility = View.GONE
        partPanelTextListOfCodes.visibility = View.GONE
        partPanelTextCodesAndCross.visibility = View.GONE

        partPanelRecycler.visibility = View.VISIBLE
        partPanelRecyclerAddButton.visibility = View.VISIBLE
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity) {
            manager = mainActivity
        }
    }
}