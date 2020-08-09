package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.PartControlType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.squareup.picasso.Picasso
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
        const val TAG = "testmt"
        const val FRAG_TAG = "partCreator"
        const val CAMERA = 2
    }

    @Inject
    lateinit var model: PartCreatorModel

    private lateinit var part: Part
    private lateinit var car: Car

    private var photo = SpecialWords.NO_PHOTO.value
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        part = arguments?.getSerializable("part") as Part
        car = arguments?.getSerializable("car") as Car
        isExist = part.id > 0
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isExist = part.id > 0
        setTitle()
        partCreatorButtonDelete.isActivated = false

        if (isExist) {
            setCreatorData()
        } else {
            partCreatorLastChangeDay.setText(dateFormatter.format(LocalDate.now()))
            partCreatorLastChangeMileage.setText(part.mileage.toString())
        }

        loadPhoto()

        partCreatorButtonCreate.setOnClickListener {
            if (partCreatorName.text.isNotEmpty() &&
                checkDataFormat(partCreatorLastChangeDay.text.toString())
            ) {

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

                if (isExist) {
                    model.partService.update(part)
                    showProgressBar()
                } else {
                    model.partService.create(part)
                    showProgressBar()
                }
            } else {
                //show snack input name or correct data 14.05.2020
            }
        }
        partCreatorButtonDelete.setOnClickListener {
            model.partService.delete(part)

            val bundle = Bundle()
            bundle.putSerializable("car", car)
            view.findNavController().navigate(R.id.action_partCreator_to_carFragment, bundle)
        }
        repairCreatorButtonCancel.setOnClickListener {
            if(isExist) {
                val bundle = Bundle()
                bundle.putSerializable("part", part)
                view.findNavController().navigate(R.id.action_partCreator_to_partFragment, bundle)
            }else{
                val bundle = Bundle()
                bundle.putSerializable("car", car)
                view.findNavController().navigate(R.id.action_partCreator_to_carFragment, bundle)
            }
        }
        partCreatorFABCreatePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
    }

    private fun showProgressBar() {
        partCreatorProgress.visibility = View.VISIBLE
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
        if(part.type == PartControlType.INSURANCE) partCreatorInsuranceBox.isChecked = true
        if(part.type == PartControlType.INSPECTION) partCreatorInspectionOnlyBox.isChecked = true
        photo = part.photo
    }

    private fun checkDataFormat(string: String): Boolean {
        return try {
            LocalDate.parse(string, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
            true
        } catch (e: Exception) {
//            Toast.makeText(this@MainActivity,"date format must be dd.MM.yyyy - 01.06.2019"
//                , Toast.LENGTH_LONG).show()   snack!!!
            false
        }
    }

    private fun loadPhoto() {
        if (photo == SpecialWords.NO_PHOTO.value) {
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

        if (photo != SpecialWords.NO_PHOTO.value) File(imagesDirectory, ("$photo.jpg")).delete()

        photo = "${part.name}${(Calendar.getInstance().timeInMillis)}"
        val f = File(imagesDirectory, ("$photo.jpg"))

        try {
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
    }

    private fun setTitle(){
        activity?.title =
            if (part.id == 0L) "Create new part"
            else "Updating ${part.name}"
    }
}