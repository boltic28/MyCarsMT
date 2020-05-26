package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.repo.repair.RepairServiceImpl
import kotlinx.android.synthetic.main.fragment_creator_repair.*

class RepairCreator (contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "repairCreator"

        fun getInstance(repair: Repair): CarCreator {

            val bundle = Bundle()
            bundle.putSerializable("repair", repair)

            val fragment = CarCreator(R.layout.fragment_creator_repair)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var repairService: RepairServiceImpl
    private lateinit var repair: Repair
    private lateinit var handler: Handler
    private var isExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repairService = RepairServiceImpl(view.context, handler)
        repair = arguments?.getSerializable("repair") as Repair
        isExist = repair.id > 0
        repairCreatorButtonDelete.isActivated = false

        if (isExist) setCreatorData()

//buttons
        repairCreatorButtonCreate.setOnClickListener {

            repair.type = repairCreatorType.text.toString()
            repair.cost = Integer.valueOf(repairCreatorCost.text.toString())
            repair.mileage = Integer.valueOf(repairCreatorMileage.text.toString())
//            repair.date = formatt
            repair.description = repairCreatorDescription.text.toString()
            if (isExist) {
                repairService.update(repair)
            } else {
                repairService.create(repair)
            }
        }
        repairCreatorButtonDelete.setOnClickListener {
            //show fragmentDialog
            repairService.delete(repair)
        }
        repairCreatorButtonCancel.setOnClickListener {
            onDestroy()
        }
    }

    private fun setCreatorData() {
        repairCreatorType.setText(repair.type)
        repairCreatorCost.setText(repair.cost)
        repairCreatorMileage.setText(repair.mileage)
        repairCreatorDescription.setText(repair.description)
//        repairCreatorLastChangeDay.setText(repair.date) format

        repairCreatorButtonCreate.setText(R.string.update)
        repairCreatorButtonDelete.isActivated = true
    }

}