package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mycarsmt.R
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.DIAGNOSTIC_IS_READY
import com.example.mycarsmt.presentation.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        const val FRAG_TAG = "settingsFragment"

        fun getInstance(): SettingFragment {
            return SettingFragment(R.layout.fragment_settings)
        }

        const val APP_PREFERENCES = "myCarPref"

        const val KM_TO_BUY = "kmToBuy"
        const val DAY_TO_BUY = "dayToBuy"
        const val KM_TO_CHANGE = "kmToChange"
        const val DAY_TO_CHANGE = "dayToChange"
        const val DAY_TO_REFRESH_INSURANCE = "insurancePeriod"
        const val DAY_TO_REFRESH_VIEW = "viewPeriod"
        const val DAY_BETWEEN_ODO_CORRECTING = "odoPeriod"
    }

    private lateinit var handler: Handler
    private lateinit var carService: CarServiceImpl

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = context?.getSharedPreferences(
            APP_PREFERENCES,
            Context.MODE_PRIVATE
        )

        initHandler(view.context)
        carService = CarServiceImpl(view.context, handler)

        settingsKmToBuyValue.setText(preferences?.getInt(KM_TO_BUY, 2000).toString())
        settingsKmToChangeValue.setText(preferences?.getInt(KM_TO_CHANGE, 1000).toString())
        settingsDayToBuyValue.setText(preferences?.getInt(DAY_TO_BUY, 20).toString())
        settingsDayToChangeValue.setText(preferences?.getInt(DAY_TO_CHANGE, 10).toString())
        settingsDaysBetweenOdoCorrectingValue.setText(
            preferences?.getInt(
                DAY_BETWEEN_ODO_CORRECTING,
                15
            ).toString()
        )

        settingsInsurancePeriodValue.setText(
            preferences?.getInt(
                DAY_TO_REFRESH_INSURANCE,
                365
            ).toString()
        )
        settingsTechViewPeriodValue.setText(
            preferences?.getInt(
                DAY_TO_REFRESH_VIEW,
                730
            ).toString()
        )

        settingsButtonSave.setOnClickListener {
            val editor = preferences?.edit()
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

            carService.doDiagnosticAllCars()
            showLoader()
        }
    }

    private fun initHandler(context: Context){
        handler = Handler(context.mainLooper, Handler.Callback { msg ->
            if(msg.what == DIAGNOSTIC_IS_READY){
                val mainActivity: Activity? = activity
                if (mainActivity is MainActivity)
                    mainActivity.loadPreviousFragment(this)
            }
                true
        })
    }

    private fun showLoader(){
        settingsButtonSave.visibility = View.GONE
        settingsLoaderBar.visibility = View.VISIBLE
        settingsLoaderText.visibility = View.VISIBLE
    }
}