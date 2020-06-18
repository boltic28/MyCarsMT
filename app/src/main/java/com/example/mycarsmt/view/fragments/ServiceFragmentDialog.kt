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
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.repo.part.PartServiceImpl
import com.example.mycarsmt.view.activities.MainActivity

class ServiceFragmentDialog : DialogFragment() {

    companion object {
        const val TAG = "testmt"
        const val FRAG_TAG = "service"

        fun getInstance(part: Part): ServiceFragmentDialog {

            val bundle = Bundle()
            bundle.putSerializable("part", part)

            val fragment = ServiceFragmentDialog()
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
        val view = inflater.inflate(R.layout.fragment_dialog_service, container, false)

        val part = arguments?.getSerializable("part") as Part
        val handler = initHandler(container!!.context.mainLooper)
        val partService = PartServiceImpl(view.context, handler)

        view.findViewById<TextView>(R.id.serviceFragmentQuestion).text =
            "Do you want to make service for: ${part.name}"

        view.findViewById<Button>(R.id.serviceFragmentButtonCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.serviceFragmentButtonMakeService).setOnClickListener {
            partService.addRepair(part.makeService())
            partService.update(part)
        }

        return view
    }

    private fun initHandler(looper: Looper): Handler {
        return Handler(looper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            if (msg.what == PartServiceImpl.RESULT_PART_UPDATED){
                val mainActivity: Activity? = activity
                if (mainActivity is MainActivity) {
                    mainActivity.loadPreviousFragmentWithStack(CarFragment.FRAG_TAG)
                }
                dismiss()
                true
            }else{
                false
            }
        })
    }
}