package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.NOTE
import com.example.mycarsmt.SpecialWords.Companion.NO_PHOTO
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.SpecialWords.Companion.REPAIR
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.enums.ContentType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.presentation.adapters.NoteItemAdapter
import com.example.mycarsmt.presentation.adapters.PartItemAdapter
import com.example.mycarsmt.presentation.adapters.RepairItemAdapter
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_car.*
import java.io.File
import javax.inject.Inject

class CarFragment @Inject constructor() : Fragment(R.layout.fragment_car) {

    companion object {
        const val TAG = "test_mt"
        const val FRAG_TAG = "carFragment"
    }

    @Inject
    lateinit var model: CarModel

    private lateinit var car: Car
    private var notes: List<Note> = emptyList()
    private var parts: List<Part> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = ContentType.PARTS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        car = arguments?.getSerializable(CAR) as Car

        Log.d(MainFragment.TAG, "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBar()
        loadModel()
        setRecycler()
        setButtons()
    }

    @SuppressLint("CheckResult")
    private fun loadModel(){
        model.carService.readById(car.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                car = value
                loadCarDataIntoView()
            }

        model.noteService.readAllForCar(car)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {value ->
                notes = value
            }

        model.partService.readAllForCar(car)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{value ->
                parts = value
            }

        model.repairService.readAllForCar(car)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{value ->
                repairs = value
            }
    }

    private fun setButtons(){
        carPanelButtonNotes.setOnClickListener {
            setContentType(ContentType.NOTES)
        }
        carPanelButtonParts.setOnClickListener {
            setContentType(ContentType.PARTS)
        }
        carPanelButtonRepairs.setOnClickListener {
            setContentType(ContentType.REPAIRS)
        }
        carPanelMileage.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CAR, car)
            view?.findNavController()?.navigate(R.id.action_carFragment_to_mileageFragmentDialog, bundle)
        }
        carPanelButtonCancel.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_carFragment_to_mainListFragment)
        }
        carPanelFABSettings.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CAR, car)
            view?.findNavController()?.navigate(R.id.action_carFragment_to_carCreator, bundle)
        }
        carPanelFABNew.setOnClickListener {
            createNew()
        }
        carPanelButtonCreateCommonParts.setOnClickListener {
            model.carService.createCommonPartsFor(car)
        }
    }

    private fun setContentType(type: ContentType){
        showProgressBar()
        contentType = type
        setAdapter()
    }

    @SuppressLint("SetTextI18n")
    private fun loadCarDataIntoView(){
        setTitle(car.getFullName())

        carPanelNumber.text = car.number
        carPanelVin.text = car.vin
        carPanelMileage.text = "${car.mileage} km"

        loadPhoto()
        refreshCondition()
        hideProgressBar()
    }

    private fun loadPhoto() {
        if (car.photo == NO_PHOTO || car.photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(carPanelMainImage)
        } else {
            Picasso.get().load(File(Directories.CAR_IMAGE_DIRECTORY.value, "${car.photo}.jpg"))
                .into(carPanelMainImage)
        }
    }

    private fun setRecycler() {
        carRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        setAdapter()
    }

    private fun setAdapter() {
        hideProgressBar()
        when (contentType) {
            ContentType.NOTES -> {
                checkOnEmpty(notes)
                car.notes = notes
                carRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        val bundle = Bundle()
                        bundle.putSerializable(CAR, car)
                        bundle.putSerializable(NOTE, note)
                        view?.findNavController()?.navigate(R.id.action_carFragment_to_noteCreator, bundle)
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
                        val bundle = Bundle()
                        bundle.putSerializable(CAR, car)
                        bundle.putSerializable(PART, part)
                        view?.findNavController()?.navigate(R.id.action_carFragment_to_partFragment, bundle)
                    }
                })
            }
            ContentType.REPAIRS -> {
                car.repairs = repairs
                checkOnEmpty(repairs)
                carRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        val bundle = Bundle()
                        bundle.putSerializable(CAR, car)
                        bundle.putSerializable(REPAIR, repair)
                        view?.findNavController()?.navigate(R.id.action_carFragment_to_repairCreator, bundle)
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

                val bundle = Bundle()
                bundle.putSerializable(NOTE, note)
                bundle.putSerializable(CAR, car)
                view?.findNavController()?.navigate(R.id.action_carFragment_to_noteCreator, bundle)

                Log.d(TAG, "pushed button - Create note")
            }
            ContentType.PARTS -> {
                val part = Part()
                part.carId = car.id
                part.mileage = car.mileage

                val bundle = Bundle()
                bundle.putSerializable(PART, part)
                bundle.putSerializable(CAR, car)
                view?.findNavController()?.navigate(R.id.action_carFragment_to_partCreator, bundle)

                Log.d(TAG, "pushed button - Create part")
            }
            ContentType.REPAIRS -> {
                val repair = Repair()
                repair.carId = car.id
                repair.mileage = car.mileage

                val bundle = Bundle()
                bundle.putSerializable(REPAIR, repair)
                bundle.putSerializable(CAR, car)
                view?.findNavController()?.navigate(R.id.action_carFragment_to_repairCreator, bundle)

                Log.d(TAG, "pushed button - Create repair")
            }
            else ->{

            }
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

    private fun turnOffIcons(){
        carPanelIconAttention.setColorFilter(Color.argb(255, 208, 208, 208))
        carPanelIconService.setColorFilter(Color.argb(255, 208, 208, 208))
        carPanelIconBuy.setColorFilter(Color.argb(255, 208, 208, 208))
        carPanelIconInfo.setColorFilter(Color.argb(255, 208, 208, 208))
    }

    private fun setTitle(title: String){
        activity?.title = title
    }

    private fun showProgressBar(){
        carProgress.visibility = View.VISIBLE
        carProgressText.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        carProgress.visibility = View.INVISIBLE
        carProgressText.visibility = View.INVISIBLE
    }
}