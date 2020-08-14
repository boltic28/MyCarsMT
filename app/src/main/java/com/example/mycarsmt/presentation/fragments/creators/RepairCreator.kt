package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.SpecialWords.Companion.REPAIR
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_creator_repair.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class RepairCreator @Inject constructor () : Fragment(R.layout.fragment_creator_repair) {

    companion object {
        const val FRAG_TAG = "repairCreator"
        const val TAG = "testmt"
    }

    @Inject
    lateinit var model: RepairCreatorModel

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    private lateinit var repair: Repair
    private var car: Car? = null
    private var part: Part? = null
    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        repair = arguments?.getSerializable(REPAIR) as Repair

        arguments?.containsKey(CAR)?.let { car = arguments?.getSerializable(CAR) as Car }
        arguments?.containsKey(PART)?.let { part = arguments?.getSerializable(PART) as Part }
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
                    bundle.putSerializable(CAR, car)
                    view.findNavController().navigate(R.id.action_repairCreator_to_carFragment, bundle)
                } else {
                    model.repairService.create(repair)
                    val bundle = Bundle()
                    bundle.putSerializable(CAR, car)
                    view.findNavController().navigate(R.id.action_repairCreator_to_carFragment, bundle)
                }
                loadPreviousFragment()
            }else{
                //snack input name and check date
            }
        }
        repairCreatorButtonDelete.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(REPAIR, repair)
            view.findNavController().navigate(R.id.action_repairCreator_to_repairDeleteDialog, bundle)
        }
        repairCreatorButtonCancel.setOnClickListener {
            view.findNavController().navigateUp()
//            val bundle = Bundle()
//            bundle.putSerializable(CAR, car)
//            view.findNavController().navigate(R.id.action_repairCreator_to_carFragment, bundle)
        }
    }

    private fun loadPreviousFragment(){
        val bundle = Bundle()
        if (car != null){
            bundle.putSerializable(CAR, car)
            view?.findNavController()?.navigate(R.id.action_noteCreator_to_carFragment, bundle)
        }
        if (part != null){
            bundle.putSerializable(PART, part)
            view?.findNavController()?.navigate(R.id.action_noteCreator_to_partFragment, bundle)
        }
    }

    @SuppressLint("CheckResult")
    private fun loadCar(){
        model.carService.readById(repair.carId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    car = it
                },{
                    Log.d(TAG, "REPAIR CREATOR: $it")
                }
            )
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