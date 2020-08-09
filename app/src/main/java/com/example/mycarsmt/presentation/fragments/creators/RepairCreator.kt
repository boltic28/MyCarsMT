package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Repair
import kotlinx.android.synthetic.main.fragment_creator_repair.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class RepairCreator @Inject constructor () : Fragment(R.layout.fragment_creator_repair) {

    private val TAG = "testmt"

    companion object {
        const val FRAG_TAG = "repairCreator"
        const val TAG = "testmt"
    }

    @Inject
    lateinit var model: RepairCreatorModel

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    private lateinit var repair: Repair
    private lateinit var car: Car
    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        repair = arguments?.getSerializable("repair") as Repair
        isExist = repair.id > 0
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        repairCreatorButtonDelete.isActivated = false

        if (isExist) {
            setCreatorData()
        }else{
            repairCreatorMileage.setText(repair.mileage.toString())
            repairCreatorDate.setText(dateFormatter.format(LocalDate.now()))
        }

        repairCreatorButtonCreate.setOnClickListener {
            if (repairCreatorType.text.isNotEmpty() &&
               checkDataFormat(repairCreatorDate.text.toString())) {
                if(repairCreatorType.text.isNotEmpty()) repair.type = repairCreatorType.text.toString()
                if(repairCreatorCost.text.isNotEmpty()) repair.cost = Integer.valueOf(repairCreatorCost.text.toString())
                if(repairCreatorMileage.text.isNotEmpty())repair.mileage = Integer.valueOf(repairCreatorMileage.text.toString())
                if(repairCreatorDate.text.isNotEmpty())repair.date = LocalDate.parse(repairCreatorDate.text.toString(), dateFormatter)
                if(repairCreatorDescription.text.isNotEmpty())repair.description = repairCreatorDescription.text.toString()

                if (isExist) {
                    model.repairService.update(repair)
                    val bundle = Bundle()
                    bundle.putSerializable("car", car)
                    view.findNavController().navigate(R.id.action_repairCreator_to_carFragment, bundle)
                } else {
                    model.repairService.create(repair)
                    val bundle = Bundle()
                    bundle.putSerializable("car", car)
                    view.findNavController().navigate(R.id.action_repairCreator_to_carFragment, bundle)
                }
            }else{
                //snack input name and check date
            }
        }
        repairCreatorButtonDelete.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("repair", repair)
            view.findNavController().navigate(R.id.action_repairCreator_to_repairDeleteDialog, bundle)
        }
        repairCreatorButtonCancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("car", car)
            view.findNavController().navigate(R.id.action_repairCreator_to_carFragment, bundle)
        }
    }

    @SuppressLint("CheckResult")
    private fun loadCar(){
        model.carService.readById(repair.carId).subscribe { car = it }
    }

    private fun setCreatorData() {
        repairCreatorType.setText(repair.type)
        repairCreatorCost.setText(repair.cost.toString())
        repairCreatorMileage.setText(repair.mileage.toString())
        repairCreatorDescription.setText(repair.description)
        repairCreatorDate.setText(dateFormatter.format(repair.date))

        repairCreatorButtonCreate.setText(R.string.update)
        repairCreatorButtonDelete.isActivated = true
    }

    private fun checkDataFormat(string: String): Boolean{
        return try {
            LocalDate.parse(string, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
            true
        }catch (e: Exception){
//            Toast.makeText(this@MainActivity,"date format must be dd.MM.yyyy - 01.06.2019"
//                , Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun setTitle(){
        activity?.title =
            if (repair.id == 0L) "Create new repair"
            else "Updating repair"
    }
}