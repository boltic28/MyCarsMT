package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CAR_CREATED
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CAR_UPDATED
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.fragments.CarDeleteDialog
import com.example.mycarsmt.view.fragments.CarFragment
import com.example.mycarsmt.view.fragments.MainListFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_creator_car.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.util.*

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

    private val CAMERA = 2
    private var photo = SpecialWords.NO_PHOTO.value
    private lateinit var manager: MainActivity
    private lateinit var carService: CarServiceImpl
    private lateinit var car: Car
    private lateinit var handler: Handler
    private var isExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentManager()

        initHandler()
        carService = CarServiceImpl(view.context, handler)
        car = arguments?.getSerializable("car") as Car
        isExist = car.id > 0
        manager.title =
            if (car.id == 0L) "Create new car"
            else "Updating ${car.brand} ${car.model}"

        carCreatorButtonDelete.isClickable = false

        if (isExist) setCreatorData()

        loadPhoto()

        carCreatorButtonCreate.setOnClickListener {
            if (carCreatorBrand.text.isNotEmpty() && carCreatorModel.text.isNotEmpty()) {
                car.brand = carCreatorBrand.text.toString()
                car.model = carCreatorModel.text.toString()
                car.photo = photo
                if (carCreatorPlateNumber.text.isNotEmpty()) car.number =
                    carCreatorPlateNumber.text.toString()
                if (carCreatorVincd.text.isNotEmpty()) car.vin =
                    carCreatorVincd.text.toString()
                if (carCreatorYear.text.isNotEmpty()) car.year =
                    Integer.valueOf(carCreatorYear.text.toString())
                if (carCreatorMileage.text.isNotEmpty()) car.mileage =
                    Integer.valueOf(carCreatorMileage.text.toString())

                if (isExist) {
                    carService.update(car)
                    showProgressBar()
                } else {
                    car.whenMileageRefreshed = LocalDate.now()
                    carService.create(car)
                    showProgressBar()
                }
            }
        }
        carCreatorButtonDelete.setOnClickListener {
            manager.loadDeleter(car)
        }
        carCreatorButtonCancel.setOnClickListener {
            manager.loadPreviousFragment()
        }
        carCreatorFABCreatePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
    }

    private fun setCreatorData() {
        carCreatorBrand.setText(car.brand)
        carCreatorModel.setText(car.model)
        carCreatorYear.setText(car.year.toString())
        carCreatorMileage.setText(car.mileage.toString())
        carCreatorVincd.setText(car.vin)
        carCreatorPlateNumber.setText(car.number)

        carCreatorButtonCreate.setText(R.string.update)
        carCreatorButtonDelete.isClickable = true
        photo = car.photo
    }

    private fun loadPhoto() {
        if (photo == SpecialWords.NO_PHOTO.value || photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(carCreatorImageOfCar)
        } else {
            Picasso.get().load(File(Directories.CAR_IMAGE_DIRECTORY.value, "$photo.jpg"))
                .into(carCreatorImageOfCar)
        }
    }

    private fun showProgressBar() {
        carCreatorProgress.visibility = View.VISIBLE
    }

    private fun initHandler() {
        handler = Handler(view!!.context.mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database...")

            when (msg.what) {
                RESULT_CAR_CREATED -> {
                    val car = msg.obj as Car
                    manager.loadCarFragmentWithoutBackStack(car)
                    Log.d(
                        TAG,
                        "Handler: ${car.brand} ${car.model} was created"
                    )

                    true
                }
                RESULT_CAR_UPDATED -> {
                    val car = msg.obj as Car
                    manager.loadPreviousFragment()
                    Log.d(
                        TAG,
                        "Handler: ${car.brand} ${car.model} was updated"
                    )

                    true
                }
                else -> {
                    false
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA) {
            val imageFromCam = data!!.extras!!.get("data") as Bitmap
            carCreatorImageOfCar.setImageBitmap(imageFromCam)
            saveImage(imageFromCam)
        }
    }

    private fun saveImage(myBitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val imagesDirectory = File(Directories.CAR_IMAGE_DIRECTORY.value)
        if (!imagesDirectory.exists()) imagesDirectory.mkdirs()
        if (photo != SpecialWords.NO_PHOTO.value) File(imagesDirectory, ("$photo.jpg")).delete()
        photo = "${car.model}_${Calendar.getInstance().timeInMillis}"

        try {
            val f = File(imagesDirectory, ("$photo.jpg"))
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            fo.close()
            Log.d(TAG, "CAR CREATOR: Image saved::--->" + f.absolutePath)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity) {
            manager = mainActivity
        }
    }
}