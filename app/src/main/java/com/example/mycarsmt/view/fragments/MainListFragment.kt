package com.example.mycarsmt.view.fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.DiagnosticElement
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.enums.ContentType
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_BUY_LIST
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CARS_READED
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_TO_DO_LIST
import com.example.mycarsmt.model.repo.note.NoteServiceImpl
import com.example.mycarsmt.model.repo.note.NoteServiceImpl.Companion.RESULT_NOTES_READED
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.adapters.CarItemAdapter
import com.example.mycarsmt.view.adapters.DiagnosticElementAdapter
import com.example.mycarsmt.view.adapters.NoteItemAdapter
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        const val FRAG_TAG = "main_Fragment"

        fun getInstance(): MainListFragment {
            return MainListFragment(R.layout.fragment_main_list)
        }
    }

    private lateinit var manager: MainActivity
    private lateinit var carService: CarServiceImpl
    private lateinit var noteService: NoteServiceImpl
    private lateinit var cars: List<Car>
    private lateinit var notes: List<Note>
    private lateinit var listToDo: List<DiagnosticElement>
    private lateinit var listToBuy: List<DiagnosticElement>
    private lateinit var handler: Handler
    private var contentType = ContentType.DEFAULT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cars = emptyList()
        notes = emptyList()
        initHandler()
        initFragmentManager()
        carService = CarServiceImpl(view.context, handler)
        noteService = NoteServiceImpl(view.context, handler)

        carService.readAll()
        carService.getToBuyList()
        carService.getToDoList()
        noteService.readAll()

        setRecycler()
        setButtons()
    }

    private fun setButtons(){
        carsAddCar.setOnClickListener {
            manager.loadCarCreator(Car())
        }

        carsSwitcher.setOnClickListener {
            contentType = if (contentType == ContentType.DEFAULT) {
                carsSwitcher.setImageResource(R.drawable.icon_car)
                ContentType.NOTES
            } else {
                carsSwitcher.setImageResource(R.drawable.ic_notes)
                ContentType.DEFAULT
            }
            setAdapter()
        }

        carsToBuy.setOnClickListener {
            contentType = ContentType.TO_BUY_LIST
            setAdapter()
        }

        carsToDo.setOnClickListener {
            contentType = ContentType.TO_DO_LIST
            setAdapter()
        }
    }

    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            when (msg.what) {
                RESULT_CARS_READED -> {
                    cars = msg.obj as List<Car>
                    Log.d(
                        TAG,
                        "Handler: took cars from database: list size ${cars.size}"
                    )

                    if(contentType == ContentType.DEFAULT) setAdapter()
                    true
                }
                RESULT_NOTES_READED -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "Handler: took notes from database: list size ${notes.size}"
                    )

                    if(contentType == ContentType.NOTES) setAdapter()
                    true
                }
                RESULT_TO_DO_LIST -> {
                    listToDo = msg.obj as List<DiagnosticElement>
                    Log.d(
                        TAG,
                        "Handler: took listToDo from database: list size ${listToDo.size}"
                    )
                    if(contentType == ContentType.TO_DO_LIST) setAdapter()
                    true
                }
                RESULT_BUY_LIST ->{
                    listToBuy = msg.obj as List<DiagnosticElement>
                    Log.d(
                        TAG,
                        "Handler: took listToBuy from database: list size ${listToBuy.size}"
                    )
                    if(contentType == ContentType.TO_BUY_LIST) setAdapter()
                    true
                }
                else -> {
                    false
                }
            }
        })
    }

    private fun listIsEmpty(list: List<*>) {
        if (list.isEmpty()) {
            carsEmptyList.visibility = View.VISIBLE
        } else {
            carsEmptyList.visibility = View.GONE
        }
    }

    private fun setRecycler() {
        firstRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        setAdapter()
    }

    private fun setAdapter() {
        when (contentType) {
            ContentType.DEFAULT -> {
                listIsEmpty(cars)
                firstRecycler.adapter = CarItemAdapter(cars, object :
                    CarItemAdapter.OnItemClickListener {
                    override fun onClick(car: Car) {
                        manager.loadCarFragment(car)
                    }
                })
            }
            ContentType.NOTES -> {
                listIsEmpty(notes)
                firstRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        manager.loadNoteCreator(note)
                    }
                })
            }
            ContentType.TO_BUY_LIST -> {
                firstRecycler.adapter = DiagnosticElementAdapter(listToBuy, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        manager.loadCarFragment(element.car)
                    }
                })
            }
            ContentType.TO_DO_LIST -> {
                firstRecycler.adapter = DiagnosticElementAdapter(listToDo, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        manager.loadCarFragment(element.car)
                    }
                })
            }
            else ->{

            }
        }
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity)
            manager = mainActivity
    }
}