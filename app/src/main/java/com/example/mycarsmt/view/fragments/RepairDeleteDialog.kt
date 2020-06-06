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
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.repo.repair.RepairServiceImpl
import com.example.mycarsmt.model.repo.repair.RepairServiceImpl.Companion.RESULT_REPAIR_CAR
import com.example.mycarsmt.view.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_delete.*

class RepairDeleteDialog : DialogFragment() {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "deleter"

        fun getInstance(repair: Repair): RepairDeleteDialog {

            val bundle = Bundle()
            bundle.putSerializable("repair", repair)

            val fragment = RepairDeleteDialog()
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
        val handler = initHandler(container!!.context.mainLooper)
        val repair = arguments?.getSerializable("repair") as Repair
        val repairService = RepairServiceImpl(view.context, handler)

        view.findViewById<TextView>(R.id.deleteFragmentQuestion).text =
            "Do you want to delete ${repair.description}"
        view.findViewById<Button>(R.id.deleteFragmentButtonCancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            repairService.delete(repair)
        }
        return view
    }

    private fun initHandler(looper: Looper): Handler {
        return Handler(looper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            if (msg.what == RESULT_REPAIR_CAR ){
                val car = msg.obj as Car
                val mainActivity: Activity? = activity
                if (mainActivity is MainActivity) {
                    mainActivity.loadPreviousFragment()
                }
                dismiss()
                true
            }else{
                false
            }
        })
    }
}