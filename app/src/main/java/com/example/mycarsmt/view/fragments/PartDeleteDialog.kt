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
import com.example.mycarsmt.model.repo.part.PartServiceImpl.Companion.RESULT_PART_CAR
import com.example.mycarsmt.view.activities.MainActivity

class PartDeleteDialog: DialogFragment() {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "deleter"

        fun getInstance(part: Part): PartDeleteDialog {

            val bundle = Bundle()
            bundle.putSerializable("part", part)

            val fragment = PartDeleteDialog()
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
        val view = inflater.inflate(R.layout.fragment_dialog_delete, container, false)
        val handler = initHandler(container!!.context.mainLooper)
        val part = arguments?.getSerializable("part") as Part
        val partService = PartServiceImpl(view.context, handler)

        view.findViewById<TextView>(R.id.deleteFragmentQuestion).text =
            "Do you want to delete ${part.name}"

        view.findViewById<Button>(R.id.deleteFragmentButtonCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            partService.delete(part)
        }

        return view
    }

    private fun initHandler(looper: Looper): Handler {
        return Handler(looper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took data from database: result " + msg.what)
            if (msg.what == RESULT_PART_CAR ){
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