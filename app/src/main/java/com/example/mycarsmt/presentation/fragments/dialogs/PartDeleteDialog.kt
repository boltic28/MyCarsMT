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
import com.example.mycarsmt.domain.service.part.PartService
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PartDeleteDialog @Inject constructor(): DialogFragment() {

   companion object {
        const val FRAG_TAG = "deleter"
        const val TAG = "testmt"
    }

    @Inject
    lateinit var carService: CarService
    @Inject
    lateinit var partService: PartService
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
            dismiss()
        }

        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            partService.delete(part).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { resUpd ->
                    Log.d(TAG, "UPDATE: $resUpd car(s) was update successful")
                    val bundle = Bundle()
                    bundle.putSerializable(CAR, car)
                    view?.findNavController()?.navigate(R.id.action_partDeleteDialog_to_carFragment, bundle)
                },
                { err ->
                    Log.d(TAG, "ERROR: car deleting is fail: $err")
                    dismiss()
                }
            )
        }

        return view
    }
}