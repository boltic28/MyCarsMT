package com.example.mycarsmt.presentation.fragments.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.mycarsmt.R
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_CAR_UPDATED
import com.example.mycarsmt.presentation.activities.MainActivity
import com.example.mycarsmt.presentation.fragments.CarFragment
import java.time.LocalDate

class MileageFragmentDialog: DialogFragment() {

    companion object {
        const val TAG = "testmt"
        const val FRAG_TAG = "mileage"

        fun getInstance(car: Car, tag: String): MileageFragmentDialog {

            val bundle = Bundle()
            bundle.putSerializable("car", car)
            bundle.putSerializable("tag", tag)

            val fragment =
                MileageFragmentDialog()
            fragment.arguments = bundle

            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dialog_mileage, container, false)

        val car = arguments?.getSerializable("car") as Car
        val handler = initHandler(container!!.context.mainLooper)
        val carService = CarServiceImpl(view.context, handler)
        val editMileage = view.findViewById<EditText>(R.id.mileageTMPOdo)

        view.findViewById<TextView>(R.id.mileageCarName).text = car.getFullName()
        editMileage.setText(car.mileage.toString())

        view.findViewById<Button>(R.id.mileageButton100).setOnClickListener {
            car.mileage += 100
            editMileage.setText(car.mileage.toString())
        }
        view.findViewById<Button>(R.id.mileageButton500).setOnClickListener {
            car.mileage += 500
            editMileage.setText(car.mileage.toString())
        }
        view.findViewById<Button>(R.id.mileageButton1000).setOnClickListener {
            car.mileage += 1000
            editMileage.setText(car.mileage.toString())
        }

        view.findViewById<Button>(R.id.mileageButtonCancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.mileageButtonOk).setOnClickListener {
            // check it on parsable
            car.mileage = editMileage.text.toString().toInt()
            car.whenMileageRefreshed = LocalDate.now()
            carService.update(car)
        }
        return view
    }

    private fun initHandler(looper: Looper): Handler {
        return Handler(looper, Handler.Callback { msg ->
            Log.d(TAG, "HandlerML: took data from database: result " + msg.what)
            val tag = arguments?.getString("tag")
            if (msg.what == RESULT_CAR_UPDATED){
                val car = msg.obj as Car
                val mainActivity: Activity? = activity
                if (mainActivity is MainActivity) {
                    if(tag.equals(CarFragment.FRAG_TAG)){
                        mainActivity.loadCarFragmentWithoutBackStack(car)
                    }else {
                        mainActivity.loadMainFragment()
                    }
                }
                dismiss()
                true
            }else{
                false
            }
        })
    }
}