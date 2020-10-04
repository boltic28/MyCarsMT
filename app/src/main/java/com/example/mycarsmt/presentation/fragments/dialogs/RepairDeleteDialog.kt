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
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.SpecialWords.Companion.REPAIR
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.domain.service.car.CarService
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.part.PartService
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairService
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import com.example.mycarsmt.presentation.fragments.CarFragment
import com.example.mycarsmt.presentation.fragments.PartFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RepairDeleteDialog @Inject constructor() : DialogFragment() {

    companion object {
        const val FRAG_TAG = "deleter"
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var repairService: RepairServiceImpl

    lateinit var repair: Repair
    lateinit var car: Car
    lateinit var part: Part
    private lateinit var target: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.injectDialog(this)
        car = arguments?.getSerializable(CAR) as Car
        part = arguments?.getSerializable(PART) as Part
        repair = arguments?.getSerializable(REPAIR) as Repair
        target = arguments?.getString(SpecialWords.TARGET, SpecialWords.MAIN).toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_delete, container, false)

        view.findViewById<TextView>(R.id.deleteFragmentQuestion).text =
            resources.getString(R.string.dialog_part_delete_really, repair.description)
        view.findViewById<Button>(R.id.deleteFragmentButtonCancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            repairService.delete(repair).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { resUpd ->
                    Log.d(TAG, "DELETE: $resUpd repair(s) was delete successful")
                    loadPreviousPage()
                },
                { err ->
                    Log.d(TAG, "ERROR: repair deleting is fail: $err")
                    dismiss()
                }
            )
        }
        return view
    }

    private fun loadPreviousPage(){
        when(target){
            PartFragment.FRAG_TAG ->{
                val bundle = Bundle()
                bundle.putSerializable(PART, part)
                view?.findNavController()?.navigate(R.id.action_repairDeleteDialog_to_partFragment)
            }
            CarFragment.FRAG_TAG -> {
                val bundle = Bundle()
                bundle.putSerializable(CAR, car)
                view?.findNavController()?.navigate(R.id.action_repairCreator_to_carFragment)
            }
        }
    }
}