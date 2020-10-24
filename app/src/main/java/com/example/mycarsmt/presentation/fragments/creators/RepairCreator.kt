package com.example.mycarsmt.presentation.fragments.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.REPAIR
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.businesslayer.Repair
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_creator_repair.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class RepairCreator @Inject constructor() : Fragment(R.layout.fragment_creator_repair) {

    companion object {
        const val FRAG_TAG = "repairCreator"
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var model: RepairCreatorModel

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    private lateinit var navController: NavController
    private lateinit var repair: Repair
    private var car: Car? = null
    private var part: Part? = null
    private var isExist = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        repair = arguments?.getSerializable(REPAIR) as Repair
        isExist = repair.id > 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        loadOwners()
        navController = view.findNavController()
        repairCreatorButtonDelete.isActivated = false

        if (isExist) {
            setCreatorData()
        } else {
            repairCreatorMileage.setText(repair.mileage.toString())
            repairCreatorDate.setText(dateFormatter.format(LocalDate.now()))
        }

        repairCreatorButtonCreate.setOnClickListener {
            isFieldsFilled().let {
                if (isExist) {
                    updateRepair()
                } else {
                    createRepair()
                }
                loadPreviousFragment()
            }

        }
        repairCreatorButtonDelete.setOnClickListener {
            loadPreviousFragment()
        }
        repairCreatorButtonCancel.setOnClickListener {
            loadPreviousFragment()
        }
    }

    private fun isFieldsFilled(): Boolean{
        if (repairCreatorType.text.isNotEmpty() &&
            checkDataFormat(repairCreatorDate.text.toString())
        ) {
            if (repairCreatorType.text.isNotEmpty()) repair.type =
                repairCreatorType.text.toString()
            if (repairCreatorCost.text.isNotEmpty()) repair.cost =
                Integer.valueOf(repairCreatorCost.text.toString())
            if (repairCreatorMileage.text.isNotEmpty()) repair.mileage =
                Integer.valueOf(repairCreatorMileage.text.toString())
            if (repairCreatorDate.text.isNotEmpty()) repair.date =
                LocalDate.parse(repairCreatorDate.text.toString(), dateFormatter)
            if (repairCreatorDescription.text.isNotEmpty()) repair.description =
                repairCreatorDescription.text.toString()

            return true
        } else {
            showMessage(resources.getString(R.string.repair_creator_bad_data))
            return false
        }
    }

    @SuppressLint("CheckResult")
    private fun createRepair() {
        model.repairService.insert(repair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { id ->
                    repair.id = id
                    Log.d(TAG, "REPAIR CREATOR: updated $id owner ${car?.number}")
                },
                { err ->
                    Log.d(TAG, "REPAIR CREATOR: $err")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun updateRepair() {
        repair.date = LocalDate.now()
        model.repairService.update(repair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "REPAIR CREATOR: updated $it")
                }, {
                    Log.d(TAG, "REPAIR CREATOR: error $it")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun loadOwners() {
        model.carService.getById(repair.carId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    car = it
                    repair.mileage = it.mileage
                    setTitle()
                }, {
                    Log.d(TAG, "REPAIR CREATOR: $it")
                }
            )

        if (repair.partId != 0L) {
            model.partRepository.getById(repair.partId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        part = it
                    }, {
                        Log.d(TAG, "REPAIR CREATOR: $it")
                    }
                )
        } else {
            part = Part()
        }
    }

    private fun loadPreviousFragment() {
        navController.navigateUp()
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

    private fun checkDataFormat(string: String): Boolean {
        return try {
            LocalDate.parse(string, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
            true
        } catch (e: Exception) {
            showMessage(resources.getString(R.string.part_creator_template_date))
            false
        }
    }

    private fun setTitle() {
        activity?.title =
            if (repair.id == 0L) resources.getString(R.string.create)
            else resources.getString(R.string.repair_creator_update)
    }

    private fun showMessage(msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
        }
    }
}