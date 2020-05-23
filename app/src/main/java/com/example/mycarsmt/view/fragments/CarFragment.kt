package com.example.mycarsmt.view.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.note.NoteServiceImpl
import com.example.mycarsmt.model.repo.part.PartServiceImpl
import com.example.mycarsmt.model.repo.repair.RepairServiceImpl
import com.example.mycarsmt.view.adaptors.NoteItemAdapter
import com.example.mycarsmt.view.adaptors.PartItemAdapter
import com.example.mycarsmt.view.adaptors.RepairItemAdapter
import kotlinx.android.synthetic.main.fragment_car.*
import kotlinx.android.synthetic.main.fragment_main_list.*

class CarFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    private lateinit var noteAdapter: NoteItemAdapter
    private lateinit var partAdapter: PartItemAdapter
    private lateinit var repairAdapter: RepairItemAdapter
    private lateinit var carService: CarServiceImpl
    private lateinit var noteService: NoteServiceImpl
    private lateinit var partService: PartServiceImpl
    private lateinit var repairService: RepairServiceImpl
    private lateinit var car: Car
    private lateinit var notes: List<Note>
    private lateinit var parts: List<Part>
    private lateinit var repairs: List<Repair>
    private lateinit var handler: Handler
    private var contentType = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        car = savedInstanceState?.get("car") as Car
        notes = emptyList()
        parts = emptyList()
        repairs = emptyList()

        initHandler()
        carService = CarServiceImpl(view.context, handler)
        noteService = NoteServiceImpl(view.context, handler)
        partService = PartServiceImpl(view.context, handler)
        repairService = RepairServiceImpl(view.context, handler)

        setRecycler()

//buttons
        but1.setOnClickListener {
            // go to car fragment
        }
        but2.setOnClickListener {
            // go to car fragment
        }
        but3.setOnClickListener {
            // go to car fragment
        }
        but4.setOnClickListener {
            // go to car fragment
        }
    }


    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took contacts from database: result " + msg.what)
            when (msg.what) {

                RESULT_CARS_READED -> {
                    cars = msg.obj as List<Car>
                    Log.d(
                        TAG,
                        "Handler: took cars from database: list size ${cars.size}"
                    )

                    listIsEmpty(cars)
                    setAdapter()
                    true
                }
                RESULT_NOTES_READED -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "Handler: took notes from database: list size ${notes.size}"
                    )

                    listIsEmpty(notes)
                    setNoteAdapter()
                    true
                }
                else -> {
                    false
                }
            }
        })
    }

    private fun setRecycler() {
        firstRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        setAdapter()
    }

    private fun setAdapter() {
        when (contentType) {
            1 -> {
                carRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        // load note fragment
                    }
                })
                noteService.readAllForCar(car.id)
            }
            2 -> {
                carRecycler.adapter = PartItemAdapter(parts, object :
                    PartItemAdapter.OnItemClickListener {
                    override fun onClick(part: Part) {
                        // load part fragment
                    }
                })
                partService.readAllForCar(car.id)
            }
            3 -> {
                carRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        // load repair fragment
                    }
                })
                repairService.readAllForCar(car.id)
            }
        }



    }
}