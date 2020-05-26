package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.repo.part.PartServiceImpl
import kotlinx.android.synthetic.main.fragment_creator_part.*

class PartCreator(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "partCreator"

        fun getInstance(part: Part): CarCreator {

            val bundle = Bundle()
            bundle.putSerializable("part", part)

            val fragment = CarCreator(R.layout.fragment_creator_part)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var partService: PartServiceImpl
    private lateinit var part: Part
    private lateinit var handler: Handler
    private var isExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        partService = PartServiceImpl(view.context, handler)
        part = arguments?.getSerializable("part") as Part
        isExist = part.id > 0
        partCreatorButtonDelete.isActivated = false

        if (isExist) setCreatorData()

//buttons
        partCreatorButtonCreate.setOnClickListener {

            part.name = partCreatorName.text.toString()
            part.codes = partCreatorCodes.text.toString()
            part.limitKM = Integer.valueOf(partCreatorLimitKm.text.toString())
            part.limitDays = Integer.valueOf(partCreatorLimitDAY.text.toString())
            part.mileageLastChange = Integer.valueOf(partCreatorLastChangeMileage.text.toString())
//            part.dateLastChange = Integer.valueOf(carCreatorMileage.text.toString()) formatter
part.description = partCreatorDescription.text.toString()
            if (isExist) {
                partService.update(part)
            } else {
                partService.create(part)
            }
        }
        partCreatorButtonDelete.setOnClickListener {
            //show fragmentDialog
            partService.delete(part)
        }
        repairCreatorButtonCancel.setOnClickListener {
            onDestroy()
        }
    }

    private fun setCreatorData() {
        partCreatorName.setText(part.name)
        partCreatorCodes.setText(part.codes)
        partCreatorLimitDAY.setText(part.limitDays)
        partCreatorLimitKm.setText(part.limitKM)
//        partCreatorLastChangeDay.setText(part.dateLastChange) format
        partCreatorLastChangeMileage.setText(part.mileage)
        partCreatorDescription.setText(part.description)
//        carCreatorImageOfCar

        partCreatorButtonCreate.setText(R.string.update)
        partCreatorButtonDelete.isActivated = true
    }

}