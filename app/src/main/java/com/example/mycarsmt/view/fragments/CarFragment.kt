package com.example.mycarsmt.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.enums.Condition
import com.example.mycarsmt.model.enums.ContentType
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_NOTES_FOR_CAR
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_PARTS_FOR_CAR
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_REPAIRS_FOR_CAR
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.adapters.NoteItemAdapter
import com.example.mycarsmt.view.adapters.PartItemAdapter
import com.example.mycarsmt.view.adapters.RepairItemAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_car.*
import java.io.File

class CarFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        const val FRAG_TAG = "carFragment"

        fun getInstance(car: Car): CarFragment {

            val bundle = Bundle()
            bundle.putSerializable("car", car)

            val fragment = CarFragment(R.layout.fragment_car)
            fragment.arguments = bundle

            return fragment
        }
    }

    private val DIAGNOSTIC_IS_READY = 121
    private lateinit var manager: MainActivity
    private lateinit var carService: CarServiceImpl
    private lateinit var car: Car
    private lateinit var handler: Handler
    //fagment
    private var notes: List<Note> = emptyList()
    private var parts: List<Part> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = ContentType.PARTS

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHandler()
        initFragmentManager()
        carService = CarServiceImpl(view.context, handler)

        car = arguments?.getSerializable("car") as Car
        loadListsForCar()
        loadCarDataIntoView()

        loadPhoto()
        setRecycler()

        carPanelButtonNotes.setOnClickListener {
            contentType = ContentType.NOTES
            setAdapter()
        }
        carPanelButtonParts.setOnClickListener {
            contentType = ContentType.PARTS
            setAdapter()
        }
        carPanelButtonRepairs.setOnClickListener {
            contentType = ContentType.REPAIRS
            setAdapter()
        }
        carPanelButtonCancel.setOnClickListener {
            manager.loadMainFragment()
        }
        carPanelFABSettings.setOnClickListener{
            manager.loadCarCreator(car)
        }
        carPanelFABNew.setOnClickListener {
            createNew()
        }
    }

    override fun onStart() {
        super.onStart()
        loadListsForCar()
    }

    private fun loadListsForCar(){
        carService.getPartsFor(car)
        carService.getNotesFor(car)
        carService.getRepairsFor(car)
    }

    @SuppressLint("SetTextI18n")
    private fun loadCarDataIntoView(){
        carPanelName.text = "${car.brand} ${car.model} (${car.year})"
        carPanelNumber.text = car.number
        carPanelVin.text = car.vin
        carPanelMileage.text = "${car.mileage} km"

        refreshCondition()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshCondition(){
        if (car.condition.contains(Condition.ATTENTION))
            carPanelIconAttention.setColorFilter(Color.argb(255, 230, 5, 5))
        if (car.condition.contains(Condition.MAKE_SERVICE))
            carPanelIconService.setColorFilter(Color.argb(255, 230, 120, 5))
        if (car.condition.contains(Condition.BUY_PARTS))
            carPanelIconBuy.setColorFilter(Color.argb(255, 180, 180, 5))
        if (car.condition.contains(Condition.MAKE_INSPECTION))
            carPanelIconInfo.setColorFilter(Color.argb(255, 10, 120, 5))

        if (car.condition.contains(Condition.CHECK_MILEAGE))
            carPanelMileage.text = "!${car.mileage} km"
    }

    @SuppressLint("SetTextI18n")
    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            when (msg.what) {

//                RESULT_CAR_UPDATED -> {
//                    car = msg.obj as Car
//                    loadCarDataIntoView()
//                    Log.d(
//                        TAG,
//                        "Handler: took updated car from database: ${car.brand} ${car.model}"
//                    )
//                    true
//                }
                RESULT_PARTS_FOR_CAR -> {
                    parts = msg.obj as List<Part>
                    Log.d(
                        TAG,
                        "Handler: took parts from database: list size ${parts.size}"
                    )
                    startDiagnostic()
                    if (contentType == ContentType.PARTS) setAdapter()
                    true
                }
                RESULT_NOTES_FOR_CAR -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "Handler: took notes from database: list size ${notes.size}"
                    )
                    carPanelNoteCount.text = "notes: ${notes.size}"
                    if (contentType == ContentType.NOTES) setAdapter()
                    true
                }
                RESULT_REPAIRS_FOR_CAR -> {
                    repairs = msg.obj as List<Repair>
                    Log.d(
                        TAG,
                        "Handler: took repairs from database: list size ${repairs.size}"
                    )
                    if (contentType == ContentType.REPAIRS) setAdapter()
                    true
                }
                DIAGNOSTIC_IS_READY ->{
                    refreshCondition()
                    carService.update(car)
                    true
                }
                else -> {
                    false
                }
            }
        })
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity)
            manager = mainActivity
    }

    private fun setRecycler() {
        carRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        setAdapter()
    }

    private fun setAdapter() {
        when (contentType) {
            ContentType.NOTES -> {
                checkOnEmpty(notes)
                car.notes = notes
                carRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        manager.loadNoteCreator(note)
                    }
                })
            }
            ContentType.PARTS -> {
                car.parts = parts
                checkOnEmpty(parts)
                carRecycler.adapter = PartItemAdapter(parts, object :
                    PartItemAdapter.OnItemClickListener {
                    override fun onClick(part: Part) {
                        manager.loadPartFragment(part)
                    }
                })
            }
            ContentType.REPAIRS -> {
                car.repairs = repairs
                checkOnEmpty(repairs)
                carRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        manager.loadRepairCreator(repair)
                    }
                })
            }
            else ->{

            }
        }
    }

    private fun checkOnEmpty(list: List<*>){
        if (list.isEmpty()) carEmptyList.visibility = View.VISIBLE
        else carEmptyList.visibility = View.GONE
    }

    private fun createNew() {
        when (contentType) {
            ContentType.NOTES -> {
                val note = Note()
                note.carId = car.id
                note.mileage = car.mileage
                manager.loadNoteCreator(note)
                Log.d(TAG, "pushed button - Create note")
            }
            ContentType.PARTS -> {
                val part = Part()
                part.carId = car.id
                part.mileage = car.mileage
                manager.loadPartCreator(part)
                Log.d(TAG, "pushed button - Create part")
            }
            ContentType.REPAIRS -> {
                val repair = Repair()
                repair.carId = car.id
                repair.mileage = car.mileage
                manager.loadRepairCreator(repair)
                Log.d(TAG, "pushed button - Create repair")
            }
            else ->{

            }
        }
    }

    private fun startDiagnostic() {
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            car.checkConditions()

            handler.sendMessage(handler.obtainMessage(DIAGNOSTIC_IS_READY))
            handlerThread.quit()
        }
    }

    private fun loadPhoto() {
        if (car.photo == SpecialWords.NO_PHOTO.value || car.photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(carPanelMainImage)
        } else {
            Picasso.get().load(File(Directories.CAR_IMAGE_DIRECTORY.value, "${car.photo}.jpg"))
                .into(carPanelMainImage)
        }
    }
}