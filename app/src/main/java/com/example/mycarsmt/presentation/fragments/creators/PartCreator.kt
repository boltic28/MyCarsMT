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
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.PartControlType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_creator_part.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class PartCreator @Inject constructor() : Fragment(R.layout.fragment_creator_part) {

    companion object {
        const val TAG = "test_mt"
        const val FRAG_TAG = "partCreator"
        const val CAMERA = 2
    }

    @Inject
    lateinit var model: PartCreatorModel

    private lateinit var part: Part
    private lateinit var car: Car
    private lateinit var navController: NavController

    private var photo = NO_PHOTO
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        part = arguments?.getSerializable(PART) as Part
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        partCreatorButtonDelete.isActivated = false

        setTitle()
        loadOwner()
        checkExisting()
        loadPhoto()
        setButtons()
    }

    @SuppressLint("CheckResult")
    private fun loadOwner(){
        model.carService.readById(part.carId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    car = it
                },{
                    Log.d(TAG, "NOTE CREATOR: $it")
                }
            )
    }

    private fun setButtons(){
        partCreatorButtonCreate.setOnClickListener {
            isFieldsFilled().let {
                showProgressBar()
                if (isExist) {
                    updatePart()
                } else {
                    createPart()
                }
            }
        }
        partCreatorButtonDelete.setOnClickListener {
            toDeleteDialog()
        }
        repairCreatorButtonCancel.setOnClickListener {
            navController.navigateUp()
        }
        partCreatorFABCreatePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
    }

    private fun setCreatorData() {
        partCreatorName.setText(part.name)
        partCreatorCodes.setText(part.codes)
        partCreatorLimitDAY.setText(part.limitDays.toString())
        partCreatorLimitKm.setText(part.limitKM.toString())
        partCreatorLastChangeDay.setText(dateFormatter.format(part.dateLastChange))
        partCreatorLastChangeMileage.setText(part.mileageLastChange.toString())
        partCreatorDescription.setText(part.description)

        partCreatorButtonCreate.setText(R.string.update)
        partCreatorButtonDelete.isActivated = true
        if (part.type == PartControlType.INSURANCE) partCreatorInsuranceBox.isChecked = true
        if (part.type == PartControlType.INSPECTION) partCreatorInspectionOnlyBox.isChecked = true
        photo = part.photo
    }

    @SuppressLint("CheckResult")
    private fun createPart() {
        car.whenMileageRefreshed = LocalDate.now()
        model.partService.create(part)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { id ->
                    part.id = id
                    showMessage("part is created")
                    toPartFragment()
                },
                { err ->
                    Log.d(TAG, "PART CREATOR: $err")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun updatePart() {
        model.partService.update(part)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { number ->
                    if (number > 0)
                        showMessage("part is updated")
                        toPartFragment()
                },
                { err ->
                    Log.d(TAG, "PART UPDATER: $err")
                }
            )
    }

    private fun toDeleteDialog() {
        val bundle = Bundle()
        bundle.putSerializable(PART, part)
        bundle.putSerializable(CAR, car)
        navController.navigate(R.id.action_partCreator_to_partDeleteDialog, bundle)
    }

    private fun toPartFragment() {
        val bundle = Bundle()
        bundle.putSerializable(PART, part)
        navController.navigate(R.id.action_partCreator_to_partFragment, bundle)
    }

    private fun isFieldsFilled(): Boolean {
        if (partCreatorName.text.isNotEmpty() &&
            checkDataFormat(partCreatorLastChangeDay.text.toString())) {

            part.name = partCreatorName.text.toString()
            part.photo = photo

            if (partCreatorCodes.text.isNotEmpty()) part.codes =
                partCreatorCodes.text.toString()
            if (partCreatorLimitKm.text.isNotEmpty()) part.limitKM =
                Integer.valueOf(partCreatorLimitKm.text.toString())
            if (partCreatorLimitDAY.text.isNotEmpty()) part.limitDays =
                Integer.valueOf(partCreatorLimitDAY.text.toString())
            if (partCreatorLastChangeMileage.text.isNotEmpty()) part.mileageLastChange =
                Integer.valueOf(partCreatorLastChangeMileage.text.toString())
            if (partCreatorLastChangeDay.text.isNotEmpty()) part.dateLastChange =
                LocalDate.parse(partCreatorLastChangeDay.text.toString(), dateFormatter)
            if (partCreatorDescription.text.isNotEmpty()) part.description =
                partCreatorDescription.text.toString()

            if (partCreatorInsuranceBox.isChecked) {
                part.type = PartControlType.INSURANCE
                part.limitDays = 365
                part.limitKM = 0
            }

            if (partCreatorInspectionOnlyBox.isChecked)
                part.type = PartControlType.INSPECTION

            return true
        } else {
            showMessage("name can't be empty!!!")
            return false
        }
    }

    private fun checkExisting() {
        isExist = part.id > 0

        if (isExist) {
            setCreatorData()
        } else {
            partCreatorLastChangeDay.setText(dateFormatter.format(LocalDate.now()))
            partCreatorLastChangeMileage.setText(part.mileage.toString())
        }
    }

    private fun showProgressBar() {
        partCreatorProgress.visibility = View.VISIBLE
    }

    private fun checkDataFormat(string: String): Boolean {
        return try {
            LocalDate.parse(string, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
            true
        } catch (e: Exception) {
            showMessage("date format must be dd.MM.yyyy - 01.06.2019")
            false
        }
    }

    private fun loadPhoto() {
        if (photo == NO_PHOTO) {
            Picasso.get().load(R.drawable.nophoto).into(partCreatorImageOfPart)
        } else {
            Picasso.get().load(File(Directories.PART_IMAGE_DIRECTORY.value, "$photo.jpg"))
                .into(partCreatorImageOfPart)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA) {
            val imageFromCam = data!!.extras!!.get("data") as Bitmap
            partCreatorImageOfPart.setImageBitmap(imageFromCam)
            saveImage(imageFromCam)
        }
    }

    private fun saveImage(myBitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val imagesDirectory = File(Directories.PART_IMAGE_DIRECTORY.value)

        if (!imagesDirectory.exists()) imagesDirectory.mkdirs()

        if (photo != NO_PHOTO) File(imagesDirectory, ("$photo.jpg")).delete()

        photo = "${part.name}${(Calendar.getInstance().timeInMillis)}"
        val f = File(imagesDirectory, ("$photo.jpg"))

        try {
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            fo.close()
            showMessage("image is saved")
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
        } catch (e1: IOException) {
            showMessage("image doesn't saved")
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
            if (part.id == 0L) "Create new part"
            else "Updating ${part.name}"
    }
}