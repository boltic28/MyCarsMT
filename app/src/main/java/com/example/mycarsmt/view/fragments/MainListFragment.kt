package com.example.mycarsmt.view.fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CARS_READED
import com.example.mycarsmt.model.repo.note.NoteServiceImpl
import com.example.mycarsmt.model.repo.note.NoteServiceImpl.Companion.RESULT_NOTES_READED
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.adaptors.CarItemAdapter
import com.example.mycarsmt.view.adaptors.NoteItemAdapter
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "main_Fragment"

        fun getInstance(): MainListFragment {
            return MainListFragment(R.layout.fragment_main_list)
        }
    }

    private lateinit var carAdapter: CarItemAdapter
    private lateinit var noteAdapter: NoteItemAdapter
    private lateinit var carService: CarServiceImpl
    private lateinit var noteService: NoteServiceImpl
    private lateinit var cars: List<Car>
    private lateinit var notes: List<Note>
    private lateinit var handler: Handler
    private var isCars = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cars = emptyList()
        notes = emptyList()
        initHandler()
        carService = CarServiceImpl(view.context, handler)
        noteService = NoteServiceImpl(view.context, handler)

//        populateBase(carService)

        if (isCars) carService.readAll()


        setRecycler()

        carsAddCar.setOnClickListener {
            // go to car fragment
        }

        carsSwitcher.setOnClickListener {
            isCars = if (isCars) {
                noteService.readAll()
                carsSwitcher.setImageResource(R.drawable.icon_car)
                false
            } else {
                carService.readAll()
                carsSwitcher.setImageResource(R.drawable.ic_notes)
                true
            }
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
                    setCarAdapter()
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
        if(isCars) {
            setCarAdapter()
        }else{
            setNoteAdapter()
        }
    }

    private fun setCarAdapter() {
        carAdapter = CarItemAdapter(cars, object :
            CarItemAdapter.OnItemClickListener {
            override fun onClick(car: Car) {
                val mainActivity: Activity? = activity
                if (mainActivity is MainActivity) {
                    val fragmentManager: FragmentManager =
                        mainActivity.getMainFragmentManager()

                    fragmentManager.beginTransaction()
                        .hide(this@MainListFragment)
                        .add(
                            R.id.fragmentContainer,
                            CarFragment.getInstance(car),
                            CarFragment.FRAG_TAG
                        )
                        .addToBackStack(CarFragment.FRAG_TAG)
                        .commit()
                }
            }
        })
        firstRecycler.adapter = carAdapter
    }

    private fun setNoteAdapter() {
        noteAdapter = NoteItemAdapter(notes, object :
            NoteItemAdapter.OnItemClickListener {
            override fun onClick(note: Note) {
                // open fragdialog done back delete
            }
        })
        firstRecycler.adapter = noteAdapter
    }

    private fun populateBase(service: CarServiceImpl) {
        val handlerThread = HandlerThread("readThread");
        handlerThread.start();
        val looper = handlerThread.looper;
        val handler = Handler(looper);
        handler.post {
            service.testing()
        }
    }

    override fun onDetach() {
        super.onDetach()

    }
}