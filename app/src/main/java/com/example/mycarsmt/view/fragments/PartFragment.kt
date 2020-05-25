package com.example.mycarsmt.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.repo.part.PartServiceImpl
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_NOTES_FOR_PART
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_REPAIRS_FOR_PART
import com.example.mycarsmt.view.adaptors.NoteItemAdapter
import com.example.mycarsmt.view.adaptors.RepairItemAdapter
import kotlinx.android.synthetic.main.fragment_part.*

class PartFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "partFragment"

        fun getInstance(part: Part): PartFragment {

            val bundle = Bundle()
            bundle.putSerializable("part", part)

            val fragment = PartFragment(R.layout.fragment_part)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var partService: PartServiceImpl
    private lateinit var part: Part
    private lateinit var handler: Handler
    private var notes: List<Note> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = 0

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHandler()
        partService = PartServiceImpl(view.context, handler)

        part = arguments?.getSerializable("part") as Part
        partService.getNotesFor(part)
        partService.getRepairsFor(part)

        partPanelName.text = part.name
        partPanelTextToChangeDKM.text = ""
        partPanelTextInstallAtKM.text = "${part.mileageLastChange} km"
        partPanelTextInstallAtDate.text = "${part.dateLastChange}"
        partPanelTextListOfCodes.text = part.codes
        partPanelTextDescriptionBody.text = part.description

        setRecycler()
        setAdapter()
//        partPanelMainImage.


//buttons
        partPanelButtonNotes.setOnClickListener {
            contentType = 1
            setAdapter()
        }
        partPanelButtonButtonRepairs.setOnClickListener {
            contentType = 2
            setAdapter()
        }
        partPanelButtonSet.setOnClickListener {
            // to part ceator
        }
        partPanelButtonCancel.setOnClickListener {
            // to car frag
        }
        partPanelRecyclerAddButton.setOnClickListener {
            when(contentType){
                1->{
                    // create note - load creator with null data
                }
                2->{
                    // create repair - load creator with null data
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            when (msg.what) {

                RESULT_NOTES_FOR_PART -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "Handler: took notes from database: list size ${notes.size}"
                    )
                    true
                }
                RESULT_REPAIRS_FOR_PART -> {
                    repairs = msg.obj as List<Repair>
                    Log.d(
                        TAG,
                        "Handler: took repairs from database: list size ${repairs.size}"
                    )
                    true
                }
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
            0 -> {
                if(partPanelRecycler.visibility == View.VISIBLE) hideRecycler ()
            }
            1 -> {
                if (partPanelRecycler.visibility == View.GONE) showRecycler()
                partPanelRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        // load noteCreator fragment
                    }
                })
            }
            2 -> {
                if (partPanelRecycler.visibility == View.GONE) showRecycler()
                partPanelRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        // load repairCreator fragment
                    }
                })
            }
        }
    }

    private fun hideRecycler() {
        partPanelRecycler.visibility = View.GONE
        partPanelRecyclerAddButton.visibility = View.GONE

        partPanelTextInstelledInfo.visibility = View.VISIBLE
        partPanelTextInstallAtDate.visibility = View.VISIBLE
        partPanelTextInstallAtKM.visibility = View.VISIBLE
        partPanelTextDescription.visibility = View.VISIBLE
        partPanelTextDescriptionBody.visibility = View.VISIBLE
        partPanelTextListOfCodes.visibility = View.VISIBLE
        partPanelTextCodesAndCross.visibility = View.VISIBLE
    }

    private fun showRecycler() {
        partPanelTextInstelledInfo.visibility = View.GONE
        partPanelTextInstallAtDate.visibility = View.GONE
        partPanelTextInstallAtKM.visibility = View.GONE
        partPanelTextDescription.visibility = View.GONE
        partPanelTextDescriptionBody.visibility = View.GONE
        partPanelTextListOfCodes.visibility = View.GONE
        partPanelTextCodesAndCross.visibility = View.GONE

        partPanelRecycler.visibility = View.VISIBLE
        partPanelRecyclerAddButton.visibility = View.VISIBLE
    }
}