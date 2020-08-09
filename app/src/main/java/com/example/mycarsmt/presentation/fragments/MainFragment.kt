package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.R
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.DiagnosticElement
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.enums.ContentType
import com.example.mycarsmt.presentation.adapters.CarItemAdapter
import com.example.mycarsmt.presentation.adapters.DiagnosticElementAdapter
import com.example.mycarsmt.presentation.adapters.NoteItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

class MainFragment @Inject constructor() : Fragment(R.layout.fragment_main) {

    companion object {
        const val FRAG_TAG = "main_Fragment"
        const val TAG = "testmt"
    }

    @Inject
    lateinit var model: MainModel

    private var cars: List<Car> = emptyList()
    private var notes: List<Note> = emptyList()
    private var listToDo: List<DiagnosticElement> = emptyList()
    private var listToBuy: List<DiagnosticElement> = emptyList()
    private var contentType = ContentType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        Log.d(TAG, "onCreate")
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        showProgressBar()

        model.carService.readAll()
            .observeOn(mainThread())
            .subscribe { value ->
                cars = value
                setAdapter()
            }

        model.noteService.readAll()
            .observeOn(mainThread())
            .subscribe { value ->
                notes = value
            }

        model.carService.getToBuyList()
            .observeOn(mainThread())
            .subscribe { value ->
                listToBuy = value
            }

        model.carService.getToDoList()
            .observeOn(mainThread())
            .subscribe { value ->
                listToBuy = value
            }

        setRecycler()
        setButtons()
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
            carsNoResult.visibility = View.GONE
            val list = cars.stream()
                .filter { car -> !car.condition.contains(Condition.OK) }
                .collect(Collectors.toList())
            setAdapter(list)
        }

        mainSearchingButtonClearFilter.setOnClickListener {
            setContentType(ContentType.DEFAULT)
        }

        carsAddCar.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mainListFragment_to_carCreator)
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
        if (list.isEmpty()) carsNoResult.visibility = View.VISIBLE
        else carsNoResult.visibility = View.GONE
        contentType = ContentType.DEFAULT
        firstRecycler.adapter = CarItemAdapter(list, object :
            CarItemAdapter.OnItemClickListener {
            override fun onClick(car: Car) {
                val bundle = Bundle()
                bundle.putSerializable("car", car)
                view?.findNavController()
                    ?.navigate(R.id.action_mainListFragment_to_carFragment, bundle)
            }

            override fun onMileageClick(car: Car) {
                val bundle = Bundle()
                bundle.putSerializable("car", car)
                view?.findNavController()
                    ?.navigate(R.id.action_carFragment_to_mileageFragmentDialog, bundle)
            }
        })
    }

    private fun setAdapter() {
        carsNoResult.visibility = View.GONE
        hideProgressBar()
        when (contentType) {
            ContentType.DEFAULT -> {
                setTitle("My cars output for ${cars.size} cars")
                listIsEmpty(cars)
                if (cars.isEmpty()) carsNoResult.visibility = View.VISIBLE
                firstRecycler.adapter = CarItemAdapter(cars, object :
                    CarItemAdapter.OnItemClickListener {
                    override fun onClick(car: Car) {
                        val bundle = Bundle()
                        bundle.putSerializable("car", car)
                        view?.findNavController()
                            ?.navigate(R.id.action_mainListFragment_to_carFragment, bundle)
                    }

                    override fun onMileageClick(car: Car) {
                        val bundle = Bundle()
                        bundle.putSerializable("car", car)
                        view?.findNavController()
                            ?.navigate(R.id.action_carFragment_to_mileageFragmentDialog, bundle)
                    }
                })
            }
            ContentType.NOTES -> {
                setTitle("My notes")
                listIsEmpty(notes)
                firstRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        val bundle = Bundle()
                        bundle.putSerializable("note", note)
                        view?.findNavController()
                            ?.navigate(R.id.action_carFragment_to_noteCreator, bundle)
                    }
                })
            }
            ContentType.TO_BUY_LIST -> {
                setTitle("Have to buy")
                listIsEmpty(listToBuy)
                firstRecycler.adapter = DiagnosticElementAdapter(listToBuy, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        val bundle = Bundle()
                        bundle.putSerializable("car", element.car)
                        view?.findNavController()
                            ?.navigate(R.id.action_carFragment_to_mileageFragmentDialog, bundle)
                    }
                })
            }
            ContentType.TO_DO_LIST -> {
                setTitle("Have to do")
                listIsEmpty(listToDo)
                firstRecycler.adapter = DiagnosticElementAdapter(listToDo, object :
                    DiagnosticElementAdapter.OnItemClickListener {
                    override fun onClick(element: DiagnosticElement) {
                        val bundle = Bundle()
                        bundle.putSerializable("car", element.car)
                        view?.findNavController()
                            ?.navigate(R.id.action_carFragment_to_mileageFragmentDialog, bundle)
                    }
                })
            }
            else -> {
            }
        }
    }

    private fun listIsEmpty(list: List<*>) {
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