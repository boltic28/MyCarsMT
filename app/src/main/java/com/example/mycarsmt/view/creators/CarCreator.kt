package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import kotlinx.android.synthetic.main.fragment_creator_car.*

class CarCreator(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "carCreator"

        fun getInstance(car: Car): CarCreator {

            val bundle = Bundle()
            bundle.putSerializable("car", car)

            val fragment = CarCreator(R.layout.fragment_creator_car)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var carService: CarServiceImpl
    private lateinit var car: Car
    private lateinit var handler: Handler
    private var isCarExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carService = CarServiceImpl(view.context, handler)
        car = arguments?.getSerializable("car") as Car
        isCarExist = car.id > 0
        carCreatorButtonDelete.isActivated = false

        if (isCarExist) setCreatorData()

//buttons
        carCreatorButtonCreate.setOnClickListener {

            car.brand = carCreatorBrand.text.toString()
            car.model = carCreatorModel.text.toString()
            car.number = carCreatorPlateNumber.text.toString()
            car.vin = carCreatorVincd.text.toString()
            car.year = Integer.valueOf(carCreatorYear.text.toString())
            car.mileage = Integer.valueOf(carCreatorMileage.text.toString())

            if (isCarExist) {
                carService.update(car)
            } else {
                carService.create(car)
            }
        }
        carCreatorButtonDelete.setOnClickListener {
            //show fragmentDialog
            carService.delete(car)
        }
        carCreatorButtonCancel.setOnClickListener {
            onDestroy()
        }
    }

    private fun setCreatorData() {
        carCreatorBrand.setText(car.brand)
        carCreatorModel.setText(car.model)
        carCreatorYear.setText(car.year)
        carCreatorMileage.setText(car.mileage)
        carCreatorVincd.setText(car.vin)
        carCreatorPlateNumber.setText(car.number)
//        carCreatorImageOfCar

        carCreatorButtonCreate.setText(R.string.update)
        carCreatorButtonDelete.isActivated = true
    }

}