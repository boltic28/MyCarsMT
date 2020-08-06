package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.enums.ContentType
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_CAR_READED
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_CAR_UPDATED
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_NOTES_FOR_CAR
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_PARTS_FOR_CAR
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_REPAIRS_FOR_CAR
import com.example.mycarsmt.presentation.activities.MainActivity
import com.example.mycarsmt.presentation.adapters.NoteItemAdapter
import com.example.mycarsmt.presentation.adapters.PartItemAdapter
import com.example.mycarsmt.presentation.adapters.RepairItemAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_car.*
import java.io.File

class CarFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    companion object {
        const val TAG = "testmt"
        const val FRAG_TAG = "carFragment"

        fun getInstance(car: Car): CarFragment {

            val bundle = Bundle()
            bundle.putSerializable("car", car)

            val fragment = CarFragment(R.layout.fragment_car)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var manager: MainActivity
    private lateinit var carService: CarServiceImpl
    private lateinit var car: Car
    private lateinit var handler: Handler
    private var notes: List<Note> = emptyList()
    private var parts: List<Part> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = ContentType.PARTS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentManager()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHandler()
        carService = CarServiceImpl()

        car = arguments?.getSerializable("car") as Car
        carService.readById(car.id)

        loadCarDataIntoView()
        setRecycler()

        setButtons()
    }

    private fun setButtons(){
        carPanelMileage.setOnClickListener {
            manager.loadMileageCorrector(car, FRAG_TAG)
        }
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
            manager.loadPreviousFragment(this)
        }
        carPanelFABSettings.setOnClickListener{
            manager.loadCarCreator(car)
        }
        carPanelFABNew.setOnClickListener {
            createNew()
        }
        carPanelButtonCreateCommonParts.setOnClickListener {
            carService.createCommonPartsFor(car)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCarDataIntoView(){
        manager.title = car.getFullName()

        carPanelNumber.text = car.number
        carPanelVin.text = car.vin
        carPanelMileage.text = "${car.mileage} km"

        loadPhoto()
        refreshCondition()
    }

    private fun loadPhoto() {
        if (car.photo == SpecialWords.NO_PHOTO.value || car.photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(carPanelMainImage)
        } else {
            Picasso.get().load(File(Directories.CAR_IMAGE_DIRECTORY.value, "${car.photo}.jpg"))
                .into(carPanelMainImage)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshCondition(){
        Log.d(TAG, "CarFragment: refreshing conditions")

        turnOffIcons()

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
        handler = Handler(view?.context?.mainLooper, Handler.Callback { msg ->
            when (msg.what) {
                RESULT_PARTS_FOR_CAR -> {
                    parts = msg.obj as List<Part>
                    Log.d(
                        TAG,
                        "HandlerCF: took parts from database: list size ${parts.size}"
                    )
                    if (contentType == ContentType.PARTS) setAdapter()
                    true
                }
                RESULT_NOTES_FOR_CAR -> {
                    notes = msg.obj as List<Note>
                    Log.d(
                        TAG,
                        "HandlerCF: took notes from database: list size ${notes.size}"
                    )
                    carPanelNoteCount.text = "notes: ${notes.size}"
                    if (contentType == ContentType.NOTES) setAdapter()
                    true
                }
                RESULT_REPAIRS_FOR_CAR -> {
                    repairs = msg.obj as List<Repair>
                    Log.d(
                        TAG,
                        "HandlerCF: took repairs from database: list size ${repairs.size}"
                    )
                    if (contentType == ContentType.REPAIRS) setAdapter()
                    true
                }
                RESULT_CAR_READED, RESULT_CAR_UPDATED -> {
                    car = msg.obj as Car
                    Log.d(
                        TAG,
                        "HandlerCF: data for car is ready"
                    )
                    loadListsForCar()
                    refreshCondition()
                    true
                }
                else -> {
                    false
                }
            }
        })
    }

    private fun loadListsForCar(){
        carService.getPartsFor(car)
        carService.getNotesFor(car)
        carService.getRepairsFor(car)
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity) {
            manager = mainActivity
        }
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
                if (parts.isEmpty()) carPanelButtonCreateCommonParts.visibility = View.VISIBLE
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
        carPanelButtonCreateCommonParts.visibility = View.INVISIBLE
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

    private fun turnOffIcons(){
        carPanelIconAttention.setColorFilter(Color.argb(255, 208, 208, 208))
        carPanelIconService.setColorFilter(Color.argb(255, 208, 208, 208))
        carPanelIconBuy.setColorFilter(Color.argb(255, 208, 208, 208))
        carPanelIconInfo.setColorFilter(Color.argb(255, 208, 208, 208))
    }
}