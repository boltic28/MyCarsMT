package com.example.mycarsmt.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.enums.Condition
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_NOTES_FOR_CAR
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_PARTS_FOR_CAR
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_REPAIRS_FOR_CAR
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.adaptors.NoteItemAdapter
import com.example.mycarsmt.view.adaptors.PartItemAdapter
import com.example.mycarsmt.view.adaptors.RepairItemAdapter
import kotlinx.android.synthetic.main.fragment_car.*

class CarFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "carFragment"

        fun getInstance(car: Car): CarFragment {

            val bundle = Bundle()
            bundle.putSerializable("car", car)

            val fragment = CarFragment(R.layout.fragment_car)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var carService: CarServiceImpl
    private lateinit var car: Car
    private lateinit var handler: Handler
    private var notes: List<Note> = emptyList()
    private var parts: List<Part> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = 2

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHandler()
        carService = CarServiceImpl(view.context, handler)

        car = arguments?.getSerializable("car") as Car
        carService.getPartsFor(car)
        carService.getNotesFor(car)
        carService.getRepairsFor(car)

        carPanelName.text = "${car.brand} ${car.model} (${car.year})"
        carPanelNumber.text = car.number
        carPanelVin.text = car.vin
        carPanelMileage.text = car.mileage.toString()

        if (car.condition.contains(Condition.ATTENTION))
            carPanelIconAttention.setColorFilter(Color.argb(255, 230, 5, 5))
        if (car.condition.contains(Condition.MAKE_SERVICE))
            carPanelIconService.setColorFilter(Color.argb(255, 230, 120, 5))
        if (car.condition.contains(Condition.BUY_PARTS))
            carPanelIconBuy.setColorFilter(Color.argb(255, 180, 180, 5))
        if (car.condition.contains(Condition.MAKE_INSPECTION))
            carPanelIconInfo.setColorFilter(Color.argb(255, 10, 120, 5))

//        carPanelMainImage.

        setRecycler()

//buttons
        but1.setOnClickListener {
            contentType = 2
            setAdapter()
        }
        but2.setOnClickListener {
            contentType = 1
            setAdapter()
        }
        but3.setOnClickListener {
            contentType = 3
            setAdapter()
        }
        but4.setOnClickListener {
            createNew()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            when (msg.what) {

                RESULT_PARTS_FOR_CAR -> {
                    parts = msg.obj as List<Part>
                    Log.d(
                        TAG,
                        "Handler: took parts from database: list size ${parts.size}"
                    )
                    startDiagnostic()
                    setAdapter()
                    true
                }
                RESULT_NOTES_FOR_CAR -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "Handler: took notes from database: list size ${notes.size}"
                    )
                    carPanelNoteCount.text = "notes: ${notes.size}"
                    true
                }
                RESULT_REPAIRS_FOR_CAR -> {
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
        carRecycler.layoutManager =
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
            }
            2 -> {
                carRecycler.adapter = PartItemAdapter(parts, object :
                    PartItemAdapter.OnItemClickListener {
                    override fun onClick(part: Part) {
                        loadPartFragment(part)
                    }
                })
            }
            3 -> {
                carRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        // load repair fragment
                    }
                })
            }
        }
    }

    private fun createNew() {
        when (contentType) {
            1 -> {
                //create note
                Log.d(TAG, "pushed button - Create note")
            }
            2 -> {
                //create part
                Log.d(TAG, "pushed button - Create part")
            }
            3 -> {
                //create repair
                Log.d(TAG, "pushed button - Create repair")
            }
        }
    }

    private fun startDiagnostic() {
        // check condition and collect it in new list in other thread
    }

    private fun loadPartFragment(part: Part){
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity) {
            val fragmentManager: FragmentManager =
                mainActivity.getMainFragmentManager()

            fragmentManager.beginTransaction()
                .hide(this@CarFragment)
                .add(
                    R.id.fragmentContainer,
                    PartFragment.getInstance(part),
                    PartFragment.FRAG_TAG
                )
                .addToBackStack(PartFragment.FRAG_TAG)
                .commit()
        }
    }

}