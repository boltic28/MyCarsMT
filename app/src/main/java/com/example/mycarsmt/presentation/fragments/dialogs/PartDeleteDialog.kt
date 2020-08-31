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
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class PartDeleteDialog @Inject constructor(): DialogFragment() {

   companion object {
        const val FRAG_TAG = "deleter"
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl

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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_delete, container, false)

        view.findViewById<TextView>(R.id.deleteFragmentQuestion).text =
            "Do you want to delete ${part.name}"

        view.findViewById<Button>(R.id.deleteFragmentButtonCancel).setOnClickListener {
            it.findNavController().navigateUp()
        }

        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            partService.delete(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { resUpd ->
                    if (part.photo != SpecialWords.NO_PHOTO)
                        File(File(Directories.PART_IMAGE_DIRECTORY.value), ("${part.photo}.jpg")).delete()
                    Log.d(TAG, "DELETE: $resUpd part(s) was delete successful")
                    val bundle = Bundle()
                    bundle.putSerializable(CAR, car)
                    it.findNavController().navigate(R.id.action_partDeleteDialog_to_carFragment, bundle)
                },
                { err ->
                    Log.d(TAG, "ERROR: part deleting is fail: $err")
                    it.findNavController().navigateUp()
                }
            )
        }

        return view
    }
}