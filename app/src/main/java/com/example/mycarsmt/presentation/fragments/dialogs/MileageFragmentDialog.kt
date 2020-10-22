package com.example.mycarsmt.presentation.fragments.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import javax.inject.Inject

class MileageFragmentDialog @Inject constructor() : DialogFragment() {

    companion object {
        const val TAG = "test_mt"
        const val FRAG_TAG = "mileage"
    }

    @Inject
    lateinit var carService: CarRepositoryImpl
    lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.injectDialog(this)
        car = arguments?.getSerializable(CAR) as Car
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dialog_mileage, container, false)

        val editMileage = view.findViewById<EditText>(R.id.mileageTMPOdo)

        view.findViewById<TextView>(R.id.mileageCarName).text = car.getFullName()
        editMileage.setText(car.mileage.toString())

        view.findViewById<Button>(R.id.mileageButton100).setOnClickListener {
            car.mileage += 100
            editMileage.setText(car.mileage.toString())
        }
        view.findViewById<Button>(R.id.mileageButton500).setOnClickListener {
            car.mileage += 500
            editMileage.setText(car.mileage.toString())
        }
        view.findViewById<Button>(R.id.mileageButton1000).setOnClickListener {
            car.mileage += 1000
            editMileage.setText(car.mileage.toString())
        }

        view.findViewById<Button>(R.id.mileageButtonCancel).setOnClickListener {
            view?.findNavController()?.navigateUp()
        }
        view.findViewById<Button>(R.id.mileageButtonOk).setOnClickListener {
            car.mileage = editMileage.text.toString().toInt()
            car.whenMileageRefreshed = LocalDate.now()
            carService.update(car)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { resUpd ->
                    Log.d(TAG, "MILEAGE: $resUpd was update successful")
                    view?.findNavController()?.navigateUp()
                },
                { err ->
                    Log.d(TAG, "ERROR: updating mileage is fail: $err")
                    view?.findNavController()?.navigateUp()
                })
        }
        return view
    }
}