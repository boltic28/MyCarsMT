package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.NOTE
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.enums.ContentType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.DiagnosticElement
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.presentation.adapters.CarItemAdapter
import com.example.mycarsmt.presentation.adapters.DiagnosticElementAdapter
import com.example.mycarsmt.presentation.adapters.NoteItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

class MainFragment @Inject constructor() : Fragment(R.layout.fragment_main) {

    companion object {
        const val FRAG_TAG = "main_Fragment"
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var model: MainModel

    private lateinit var navController: NavController
    private var contentType = ContentType.DEFAULT
    private var listToDo: List<DiagnosticElement> = emptyList()
    private var listToBuy: List<DiagnosticElement> = emptyList()
    private var cars: List<Car> = emptyList()
    private var notes: List<Note> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        Log.d(TAG, "MAIN: onCreate")
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "MAIN: onViewCreated")
        navController = view.findNavController()

        showProgressBar()
        setRecycler()
        setButtons()
        loadData()
    }

    @SuppressLint("CheckResult")
    private fun loadData(){
        model.carService.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                cars = list
                setAdapter()
                cars.forEach { getPartsForCar(it) }
            }

        model.noteService.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                notes = list
            }

        model.carService.getToBuyList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                listToBuy = list
            }

        model.carService.getToDoList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                listToDo = list
            }
    }

    @SuppressLint("CheckResult")
    fun getPartsForCar(car: Car){
        model.partService.getAllForCar(car)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                car.parts = it
            }, {
                Log.d(TAG, "MAIN: Error $it")
            })
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
            setContentType(ContentType.DEFAULT)
        }

        mainSearchingButtonNotes.setOnClickListener {
            setContentType(ContentType.NOTES)
        }

        mainSearchingButtonBuy.setOnClickListener {
            setContentType(ContentType.TO_BUY_LIST)
        }

        mainSearchingButtonService.setOnClickListener {
            setContentType(ContentType.TO_DO_LIST)
        }

        mainSearchingButtonAttention.setOnClickListener {
            findCarsWithProblem()
        }

        mainSearchingButtonClearFilter.setOnClickListener {
            setContentType(ContentType.DEFAULT)
        }

        carsAddCar.setOnClickListener {
            toCarCreator()
        }
    }

    private fun setContentType(type: ContentType) {
        showProgressBar()
        contentType = type
        setAdapter()
    }

    private fun setRecycler() {
        firstRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setAdapter(list: MutableList<Car>) {
        setTitle("My cars output for ${list.size} cars")
        isListEmpty(list)
        contentType = ContentType.DEFAULT

        firstRecycler.adapter = CarItemAdapter(list, object :
            CarItemAdapter.OnItemClickListener {
            override fun onClick(car: Car) {
                toCarFragment(car)
            }

            override fun onMileageClick(car: Car) {
                toMileageDialog(car)
            }
        })
    }

    private fun setAdapter() {
        hideProgressBar()
        when (contentType) {
            ContentType.DEFAULT -> {
                setTitle("My cars output for ${cars.size} cars")
                isListEmpty(cars)
                firstRecycler.adapter = CarItemAdapter(cars, object :
                    CarItemAdapter.OnItemClickListener {
                    override fun onClick(car: Car) {
                        toCarFragment(car)
                    }

                    override fun onMileageClick(car: Car) {
                        toMileageDialog(car)
                    }
                })
            }
            ContentType.NOTES -> {
                setTitle("My notes")
                isListEmpty(notes)
                firstRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        toNoteFragment(note)
                    }
                })
            }
            ContentType.TO_BUY_LIST -> {
                setTitle("Have to buy")
                isListEmpty(listToBuy)
                firstRecycler.adapter = DiagnosticElementAdapter(listToBuy, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        toCarFragment(element.car)
                    }
                })
            }
            ContentType.TO_DO_LIST -> {
                setTitle("Have to do")
                isListEmpty(listToDo)
                firstRecycler.adapter = DiagnosticElementAdapter(listToDo, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        toCarFragment(element.car)
                    }
                })
            }
            else -> {
            }
        }
    }

    private fun findCarsWithProblem() {
        val list = cars.stream()
            .filter { car -> !car.condition.contains(Condition.OK) }
            .collect(Collectors.toList())
        setAdapter(list)
    }

    private fun toCarFragment(car: Car) {
        val bundle = Bundle()
        bundle.putSerializable(CAR, car)
        navController.navigate(R.id.action_mainListFragment_to_carFragment, bundle)
    }

    private fun toNoteFragment(note: Note) {
        val bundle = Bundle()
        bundle.putSerializable(NOTE, note)
        navController.navigate(R.id.action_mainListFragment_to_noteCreator, bundle)
    }

    private fun toMileageDialog(car: Car) {
        val bundle = Bundle()
        bundle.putSerializable(CAR, car)
        navController.navigate(R.id.action_mainListFragment_to_mileageFragmentDialog, bundle)
    }

    private fun toCarCreator() {
        val bundle = Bundle()
        bundle.putSerializable(CAR, Car())
        navController.navigate(R.id.action_mainListFragment_to_carCreator, bundle)
    }

    private fun isListEmpty(list: List<*>) {
        if (list.isEmpty()) {
            carsEmptyList.visibility = View.VISIBLE
        } else {
            carsEmptyList.visibility = View.GONE
        }
    }

    private fun setTitle(title: String) {
        activity?.title = title
    }

    private fun showProgressBar() {
        carsProgress.visibility = View.VISIBLE
        carsProgressText.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        carsProgress.visibility = View.INVISIBLE
        carsProgressText.visibility = View.INVISIBLE
    }
}