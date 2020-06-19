package com.example.mycarsmt.view.fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.DiagnosticElement
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.enums.Condition
import com.example.mycarsmt.model.enums.ContentType
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.DIAGNOSTIC_IS_READY
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_BUY_LIST
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CARS_READED
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CAR_READED
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CAR_UPDATED
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_TO_DO_LIST
import com.example.mycarsmt.model.repo.note.NoteServiceImpl
import com.example.mycarsmt.model.repo.note.NoteServiceImpl.Companion.RESULT_NOTES_READED
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.adapters.CarItemAdapter
import com.example.mycarsmt.view.adapters.DiagnosticElementAdapter
import com.example.mycarsmt.view.adapters.NoteItemAdapter
import kotlinx.android.synthetic.main.fragment_main_list.*
import java.util.*
import java.util.stream.Collectors

class MainListFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    companion object {
        const val FRAG_TAG = "main_Fragment"
        const val TAG = "testmt"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        initFragmentManager()

        cars = emptyList()
        notes = emptyList()

        initHandler()
        manager.title = "My Cars"

        carService = CarServiceImpl(view.context, handler)
        noteService = NoteServiceImpl(view.context, handler)

        carService.readAll()
        carService.getToBuyList()
        carService.getToDoList()
        noteService.readAll()

        setRecycler()
        setButtons()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    private fun setButtons() {
        mainSearchingText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                val findingText = mainSearchingText.text.toString()
                if (findingText.isNotEmpty()) {
                    val list = cars.stream()
                        .filter { car ->
                            car.getFullName().toLowerCase(Locale.ROOT).contains(findingText)
                        }
                        .collect(Collectors.toList())
                    setAdapter(list)
                } else {
                    contentType = ContentType.DEFAULT
                    setAdapter()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        mainSearchingButtonCars.setOnClickListener {
            contentType = ContentType.DEFAULT
            setAdapter()
        }

        mainSearchingButtonNotes.setOnClickListener {
            contentType = ContentType.NOTES
            setAdapter()
        }

        mainSearchingButtonBuy.setOnClickListener {
            contentType = ContentType.TO_BUY_LIST
            setAdapter()
        }

        mainSearchingButtonService.setOnClickListener {
            contentType = ContentType.TO_DO_LIST
            setAdapter()
        }

        mainSearchingButtonAttention.setOnClickListener {
            carsNoResult.visibility = View.GONE
            val list = cars.stream()
                .filter { car -> !car.condition.contains(Condition.OK) }
                .collect(Collectors.toList())
            setAdapter(list)
        }

        mainSearchingButtonClearFilter.setOnClickListener {
            contentType = ContentType.DEFAULT
            setAdapter()
        }

        carsAddCar.setOnClickListener {
            manager.loadCarCreator(Car())
        }

        carsCreateTestPark.setOnClickListener {
            carsCreateTestPark.visibility = View.GONE
            carService.createTestEntityForApp()
        }
    }


    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            when (msg.what) {
                RESULT_CARS_READED -> {
                    cars = msg.obj as List<Car>
                    Log.d(
                        TAG,
                        "HandlerMF: took cars from database: list size ${cars.size}"
                    )
                    if (contentType == ContentType.DEFAULT) setAdapter()
                    true
                }
                DIAGNOSTIC_IS_READY -> {
                    carsCreateTestPark.visibility = View.GONE
                    carsNoResult.visibility = View.GONE
                    cars = msg.obj as List<Car>
                    Log.d(
                        TAG,
                        "HandlerMF: took cars from database after diagnostic: list size ${cars.size}"
                    )

                    if (contentType == ContentType.DEFAULT) setAdapter()
                    true
                }
                RESULT_NOTES_READED -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "HandlerMF: took notes from database: list size ${notes.size}"
                    )
                    if (contentType == ContentType.NOTES) setAdapter()
                    // turn off diagnostic loading 30%
                    true
                }
                RESULT_TO_DO_LIST -> {
                    listToDo = msg.obj as List<DiagnosticElement>
                    Log.d(
                        TAG,
                        "HandlerMF: took listToDo from database: list size ${listToDo.size}"
                    )
                    if (contentType == ContentType.TO_DO_LIST) setAdapter()
                    // turn off diagnostic loading 30%
                    true
                }
                RESULT_BUY_LIST -> {
                    listToBuy = msg.obj as List<DiagnosticElement>
                    Log.d(
                        TAG,
                        "HandlerMF: took listToBuy from database: list size ${listToBuy.size}"
                    )
                    if (contentType == ContentType.TO_BUY_LIST) setAdapter()
                    // turn off diagnostic loading 30%
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

    private fun setAdapter(list: MutableList<Car>) {
        if (list.isEmpty()) carsNoResult.visibility = View.VISIBLE
        else carsNoResult.visibility = View.GONE
        contentType = ContentType.DEFAULT
        firstRecycler.adapter = CarItemAdapter(list, object :
            CarItemAdapter.OnItemClickListener {
            override fun onClick(car: Car) {
                manager.loadCarFragment(car)
            }

            override fun onMileageClick(car: Car) {
                manager.loadMileageCorrector(car, FRAG_TAG)
            }
        })
    }

    private fun setAdapter() {
        carsNoResult.visibility = View.GONE
        carsCreateTestPark.visibility = View.GONE
        when (contentType) {
            ContentType.DEFAULT -> {
                listIsEmpty(cars)
                if (cars.isEmpty()) carsCreateTestPark.visibility = View.VISIBLE
                firstRecycler.adapter = CarItemAdapter(cars, object :
                    CarItemAdapter.OnItemClickListener {
                    override fun onClick(car: Car) {
                        manager.loadCarFragment(car)
                    }

                    override fun onMileageClick(car: Car) {
                        manager.loadMileageCorrector(car, FRAG_TAG)
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
                listIsEmpty(listToBuy)
                firstRecycler.adapter = DiagnosticElementAdapter(listToBuy, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        manager.loadCarFragment(element.car)
                    }
                })
            }
            ContentType.TO_DO_LIST -> {
                listIsEmpty(listToDo)
                firstRecycler.adapter = DiagnosticElementAdapter(listToDo, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        manager.loadCarFragment(element.car)
                    }
                })
            }
            else -> {

            }
        }
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity) {
            manager = mainActivity
        }
    }
}