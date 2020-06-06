package com.example.mycarsmt.view.fragments

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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.model.repo.car.CarServiceImpl.Companion.RESULT_CAR_DELETED
import com.example.mycarsmt.view.activities.MainActivity

class CarDeleteDialog : DialogFragment() {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "deleter"

        fun getInstance(car: Car): CarDeleteDialog {

            val bundle = Bundle()
            bundle.putSerializable("car", car)

            val fragment = CarDeleteDialog()
            fragment.arguments = bundle

            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delete, container, false)

        val car = arguments?.getSerializable("car") as Car
        val handler = initHandler(container!!.context.mainLooper)
        val carService = CarServiceImpl(view.context, handler)

        view.findViewById<TextView>(R.id.deleteFragmentQuestion).text =
            "Do you want to delete ${car.brand} ${car.model}"
        view.findViewById<Button>(R.id.deleteFragmentButtonCancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            carService.delete(car)
        }
        return view
    }

    private fun initHandler(looper: Looper): Handler {
        return Handler(looper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            if (msg.what == RESULT_CAR_DELETED ){
                val mainActivity: Activity? = activity
                if (mainActivity is MainActivity) {
                    mainActivity.loadMainFragment()
                }
                dismiss()
                true
            }else{
                false
            }
        })
    }
}