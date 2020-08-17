package com.example.mycarsmt.presentation.fragments.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.car.CarService
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.part.PartService
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ServiceFragmentDialog @Inject constructor(): DialogFragment() {

    companion object {
        const val TAG = "test_mt"
        const val FRAG_TAG = "service"
    }

    @Inject
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var repairService: RepairServiceImpl

    lateinit var car: Car
    lateinit var part: Part

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.injectDialog(this)
        car = arguments?.getSerializable(CAR) as Car
        part = arguments?.getSerializable(PART) as Part
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dialog_service, container, false)

        view.findViewById<TextView>(R.id.serviceFragmentQuestion).text =
            "Do you want to make service for: ${part.name}"

        view.findViewById<Button>(R.id.serviceFragmentButtonCancel).setOnClickListener {
            view?.findNavController()?.navigateUp()
        }

        view.findViewById<Button>(R.id.serviceFragmentButtonMakeService).setOnClickListener {
            repairService.create(part.makeService())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            partService.update(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { result ->
                    Log.d(TAG, "SERVICE: done successful: $result")
                    val bundle = Bundle()
                    bundle.putSerializable(CAR, car)
                    view?.findNavController()?.navigate(R.id.action_serviceFragmentDialog_to_carFragment, bundle)
                },
                { err ->
                    Log.d(TAG, "ERROR: service is fail: $err")
                    view?.findNavController()?.navigateUp()
                }
            )
        }

        return view
    }
}