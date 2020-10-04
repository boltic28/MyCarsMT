package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mycarsmt.R
import com.example.mycarsmt.backServices.TXTHelper
import com.example.mycarsmt.dagger.App
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingFragment @Inject constructor() : Fragment(R.layout.fragment_settings) {

    companion object {
        const val FRAG_TAG = "settingsFragment"
        const val TAG = "test_mt"

        const val APP_PREFERENCES = "myCarPref"
        const val KM_TO_BUY = "kmToBuy"
        const val DAY_TO_BUY = "dayToBuy"
        const val KM_TO_CHANGE = "kmToChange"
        const val DAY_TO_CHANGE = "dayToChange"
        const val DAY_TO_REFRESH_INSURANCE = "insurancePeriod"
        const val DAY_TO_REFRESH_VIEW = "viewPeriod"
        const val DAY_BETWEEN_ODO_CORRECTING = "odoPeriod"
    }

    @Inject
    lateinit var model: SettingModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsKmToBuyValue.setText(model.preferences.getInt(KM_TO_BUY, 2000).toString())
        settingsKmToChangeValue.setText(model.preferences.getInt(KM_TO_CHANGE, 1000).toString())
        settingsDayToBuyValue.setText(model.preferences.getInt(DAY_TO_BUY, 20).toString())
        settingsDayToChangeValue.setText(model.preferences.getInt(DAY_TO_CHANGE, 10).toString())
        settingsDaysBetweenOdoCorrectingValue.setText(
            model.preferences.getInt(
                DAY_BETWEEN_ODO_CORRECTING,
                15
            ).toString()
        )

        settingsInsurancePeriodValue.setText(
            model.preferences.getInt(
                DAY_TO_REFRESH_INSURANCE,
                365
            ).toString()
        )
        settingsTechViewPeriodValue.setText(
            model.preferences.getInt(
                DAY_TO_REFRESH_VIEW,
                730
            ).toString()
        )

        settingsButtonSave.setOnClickListener {
            val editor = model.preferences.edit()
            editor?.putInt(KM_TO_BUY, Integer.valueOf(settingsKmToBuyValue.text.toString()))
            editor?.putInt(KM_TO_CHANGE, Integer.valueOf(settingsKmToChangeValue.text.toString()))
            editor?.putInt(DAY_TO_BUY, Integer.valueOf(settingsDayToBuyValue.text.toString()))
            editor?.putInt(DAY_TO_CHANGE, Integer.valueOf(settingsDayToChangeValue.text.toString()))
            editor?.putInt(
                DAY_TO_REFRESH_INSURANCE,
                Integer.valueOf(settingsInsurancePeriodValue.text.toString())
            )
            editor?.putInt(
                DAY_TO_REFRESH_VIEW,
                Integer.valueOf(settingsTechViewPeriodValue.text.toString())
            )
            editor?.putInt(
                DAY_BETWEEN_ODO_CORRECTING,
                Integer.valueOf(settingsDaysBetweenOdoCorrectingValue.text.toString())
            )

            editor?.apply()

            showProgressBar()
            refreshCars()
        }

        settingsButtonSaveToFile.setOnClickListener {
            showProgressBar()
            val fileHelper = TXTHelper()
            fileHelper.writeCarsToFile()
                .subscribe({
                    showMessage(it)
                    view.findNavController()
                        .navigate(R.id.action_settingFragment_to_mainListFragment)
                }, { err ->
                    Log.d(TAG, "ERROR: writing data from file -> $err")
                })
        }

        settingsButtonReadCarsFromFile.setOnClickListener {
            showProgressBar()
            val fileHelper = TXTHelper()
            fileHelper.readDataFromFile()
                .subscribe({
                    refreshCars()
                }, { err ->
                    Log.d(TAG, "ERROR: reading data from file -> $err")
                })
        }
    }

    @SuppressLint("CheckResult")
    private fun refreshCars() {

        model.carService.refreshAll()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.findNavController()?.
                    navigate(R.id.action_settingFragment_to_mainListFragment)
            }, { err ->
                Log.d(TAG, "ERROR: doDiagnosticForAllCars -> writing in DB: $err")
            })
    }

    private fun showMessage(msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showProgressBar() {
        settingsLoadingCarsFromFileText.visibility = View.INVISIBLE
        settingsSavingCarsToFileText.visibility = View.INVISIBLE
        settingsButtonReadCarsFromFile.visibility = View.INVISIBLE
        settingsButtonSaveToFile.visibility = View.INVISIBLE
        settingsButtonSave.visibility = View.INVISIBLE
        settingsLoaderBar.visibility = View.VISIBLE
        settingsLoaderText.visibility = View.VISIBLE
    }
}