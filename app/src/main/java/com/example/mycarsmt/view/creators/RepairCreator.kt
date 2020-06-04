package com.example.mycarsmt.view.creators

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.repo.repair.RepairServiceImpl
import com.example.mycarsmt.view.activities.MainActivity
import com.example.mycarsmt.view.fragments.CarFragment
import com.example.mycarsmt.view.fragments.PartFragment
import com.example.mycarsmt.view.fragments.RepairDeleteDialog
import kotlinx.android.synthetic.main.fragment_creator_repair.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RepairCreator (contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val TAG = "testmt"

    companion object {
        val FRAG_TAG = "repairCreator"

        fun getInstance(repair: Repair): RepairCreator {

            val bundle = Bundle()
            bundle.putSerializable("repair", repair)

            val fragment = RepairCreator(R.layout.fragment_creator_repair)
            fragment.arguments = bundle

            return fragment
        }
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    private lateinit var manager: MainActivity
    private lateinit var repairService: RepairServiceImpl
    private lateinit var repair: Repair
    private lateinit var handler: Handler
    private var isExist = true

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentManager()
        handler = Handler(view.context.mainLooper)
        repairService = RepairServiceImpl(view.context, handler)
        repair = arguments?.getSerializable("repair") as Repair
        isExist = repair.id > 0
        repairCreatorButtonDelete.isActivated = false

        if (isExist) {
            setCreatorData()
        }else{
            repairCreatorMileage.setText(repair.mileage.toString())
            repairCreatorDate.setText(dateFormatter.format(LocalDate.now()))
        }

        repairCreatorButtonCreate.setOnClickListener {
            if (repairCreatorType.text.isNotEmpty() &&
               checkDataFormat(repairCreatorDate.text.toString())) {
                if(repairCreatorType.text.isNotEmpty()) repair.type = repairCreatorType.text.toString()
                if(repairCreatorCost.text.isNotEmpty()) repair.cost = Integer.valueOf(repairCreatorCost.text.toString())
                if(repairCreatorMileage.text.isNotEmpty())repair.mileage = Integer.valueOf(repairCreatorMileage.text.toString())
                if(repairCreatorDate.text.isNotEmpty())repair.date = LocalDate.parse(repairCreatorDate.text.toString(), dateFormatter)
                if(repairCreatorDescription.text.isNotEmpty())repair.description = repairCreatorDescription.text.toString()

                if (isExist) {
                    repairService.update(repair)
                    manager.loadPreviousFragment()
                } else {
                    repairService.create(repair)
                    manager.loadPreviousFragment()
                }
            }else{
                //snack input name and check date
            }
        }
        repairCreatorButtonDelete.setOnClickListener {
            manager.loadDeleter(repair)
        }
        repairCreatorButtonCancel.setOnClickListener {
            manager.loadPreviousFragment()
        }
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

    private fun checkDataFormat(string: String): Boolean{
        return try {
            LocalDate.parse(string, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
            true
        }catch (e: Exception){
//            Toast.makeText(this@MainActivity,"date format must be dd.MM.yyyy - 01.06.2019"
//                , Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun initFragmentManager() {
        val mainActivity: Activity? = activity
        if (mainActivity is MainActivity)
            manager = mainActivity
    }
}