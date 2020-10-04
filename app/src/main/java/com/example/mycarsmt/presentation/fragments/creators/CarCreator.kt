package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.NO_PHOTO
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_creator_car.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class CarCreator @Inject constructor() : Fragment(R.layout.fragment_creator_car) {

    companion object {
        const val FRAG_TAG = "carCreator"
        const val TAG = "test_mt"
        const val CAMERA = 2
    }

    @Inject
    lateinit var model: CarCreatorModel

    private lateinit var car: Car
    private var photo = NO_PHOTO
    private var isExist = true
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        car = arguments?.getSerializable(CAR) as Car
        isExist = car.id > 0
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        setTitle()
        loadPhoto()
        carCreatorButtonDelete.isClickable = false

        if (isExist) setCreatorData()

        carCreatorButtonCreate.setOnClickListener {
            isFieldsFilled().let {
                showProgressBar()
                if (isExist) {
                    updateCar()
                } else {
                    createCar()
                }
            }
        }

        carCreatorButtonDelete.setOnClickListener {
            toDeleteDialog()
        }

        carCreatorButtonCancel.setOnClickListener {
            toPreviousFragment()
        }

        carCreatorFABCreatePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
    }

    private fun isFieldsFilled(): Boolean{
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
            return true
        }else{
            showMessage(resources.getString(R.string.car_creator_message_wrong_brand_model))
            return false
        }
    }

    @SuppressLint("CheckResult")
    private fun createCar() {
        car.whenMileageRefreshed = LocalDate.now()
        model.carService.create(car)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { id ->
                    car.id = id
                    toCarFragment()
                    showMessage(resources.getString(R.string.car_creator_car_created))
                },
                { err ->
                    Log.d(TAG, "CAR CREATOR: $err")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun updateCar() {
        model.carService.update(car)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { number ->
                    if (number > 0)
                        toCarFragment()
                    showMessage(resources.getString(R.string.car_creator_car_updated))
                },
                { err ->
                    Log.d(TAG, "CAR UPDATER: $err")
                }
            )
    }

    private fun toDeleteDialog(){
        val bundle = Bundle()
        bundle.putSerializable(CAR, car)
        navController.navigate(R.id.action_carCreator_to_carDeleteDialog, bundle)
    }

    private fun toPreviousFragment(){
        if (isExist) {
            navController.navigateUp()
        } else {
            navController.navigate(R.id.action_carCreator_to_mainListFragment)
        }
    }

    private fun toCarFragment() {
        val bundle = Bundle()
        bundle.putSerializable(CAR, car)
        navController.navigate(R.id.action_carCreator_to_carFragment, bundle)
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
        if (photo == NO_PHOTO || photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(carCreatorImageOfCar)
        } else {
            Picasso.get()
                .load(File(Directories.CAR_IMAGE_DIRECTORY.value,
                    resources.getString(R.string.photo_path, photo)))
                .into(carCreatorImageOfCar)
        }
    }

    private fun showProgressBar() {
        carCreatorProgress.visibility = View.VISIBLE
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
        if (photo != NO_PHOTO) File(imagesDirectory, (resources.getString(R.string.photo_path, photo))).delete()
        photo = "${car.model}_${Calendar.getInstance().timeInMillis}"

        try {
            val f = File(imagesDirectory, (resources.getString(R.string.photo_path, photo)))
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            fo.close()
            Log.d(TAG, "CAR CREATOR: Image saved::--->" + f.absolutePath)
            showMessage(resources.getString(R.string.car_creator_image_saved))
        } catch (e1: IOException) {
            showMessage(resources.getString(R.string.car_creator_image_not_saved))
            e1.printStackTrace()
        }
    }

    private fun showMessage(msg: String){
        view?.let {
            Snackbar.make(it,msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setTitle() {
        activity?.title =
            if (car.id == 0L) resources.getString(R.string.car_creator_create_new)
            else resources.getString(R.string.car_creator_update_car, car.brand, car.model)
    }
}