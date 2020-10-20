package com.example.mycarsmt.domain

import android.content.SharedPreferences
import com.example.mycarsmt.SpecialWords.Companion.NO_PHOTO
import com.example.mycarsmt.datalayer.enums.Condition
import com.example.mycarsmt.datalayer.enums.PartControlType
import com.example.mycarsmt.presentation.fragments.SettingFragment
import java.io.Serializable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Part(): Serializable {

    private var warnKMToBuy = 2000
    private var warnKMToService = 1000
    private var warnDayToBuy = 30
    private var warnDayToService = 10
    private var insurancePeriod = 365
    private var viewPeriod = 365

    constructor(
        id: Long,
        carId: Long,
        mileage: Int,
        name: String,
        codes: String,
        limitKM: Int,
        limitDays: Int,
        dateLastChange: LocalDate,
        mileageLastChange: Int,
        description: String,
        photo: String,
        type: PartControlType,
        condition: List<Condition>
    ) : this() {
        this.id = id
        this.carId = carId
        this.mileage = mileage
        this.name = name
        this.codes = codes
        this.limitKM = limitKM
        this.limitDays = limitDays
        this.dateLastChange = dateLastChange
        this.mileageLastChange = mileageLastChange
        this.description = description
        this.photo = photo
        this.type = type
        this.condition = condition
    }

    constructor(
        id: Long,
        carId: Long,
        name: String,
        codes: String,
        limitKM: Int,
        limitDays: Int,
        dateLastChange: LocalDate,
        mileageLastChange: Int,
        description: String,
        photo: String,
        type: PartControlType,
        condition: List<Condition>
    ) : this() {
        this.id = id
        this.carId = carId
        this.name = name
        this.codes = codes
        this.limitKM = limitKM
        this.limitDays = limitDays
        this.dateLastChange = dateLastChange
        this.mileageLastChange = mileageLastChange
        this.description = description
        this.photo = photo
        this.type = type
        this.condition = condition
    }

    constructor(
        carId: Long,
        mileage: Int,
        name: String,
        limitKM: Int,
        limitDays: Int,
        type: PartControlType
    ) : this() {
        this.carId = carId
        this.mileage = mileage
        this.name = name
        this.limitKM = limitKM
        this.limitDays = limitDays
        this.type = type
        mileageLastChange = mileage
    }

    var id: Long = 0

    var carId: Long = 0
    var mileage: Int = 0
    var name: String = "part"
    var codes: String = "no code"
    var limitKM: Int = 10000
    var limitDays: Int = 365
    var dateLastChange: LocalDate = LocalDate.now()
    var mileageLastChange: Int = 0
    var description: String = "description"
    var photo = NO_PHOTO
    var type: PartControlType = PartControlType.CHANGE
    var condition: List<Condition> = listOf(Condition.OK)

    var notes: List<Note> = emptyList()
    var repairs: List<Repair> = emptyList()

    fun checkCondition(preferences: SharedPreferences){

        warnKMToBuy = preferences.getInt(SettingFragment.KM_TO_BUY, 2000)
        warnKMToService = preferences.getInt(SettingFragment.KM_TO_CHANGE, 1000)
        warnDayToBuy = preferences.getInt(SettingFragment.DAY_TO_BUY, 30)
        warnDayToService = preferences.getInt(SettingFragment.DAY_TO_CHANGE, 10)
        insurancePeriod = preferences.getInt(SettingFragment.DAY_TO_REFRESH_INSURANCE, 365)
        viewPeriod = preferences.getInt(SettingFragment.DAY_TO_REFRESH_VIEW, 365)

        val list = mutableListOf<Condition>()
        if (isNeedToInspection()) list.add(Condition.MAKE_INSPECTION)
        if (isNeedToBuy()) list.add(Condition.BUY_PARTS)
        if (isNeedToService()) list.add(Condition.MAKE_SERVICE)
        if (isOverRide()) list.add(Condition.OVERUSED)
        if (list.isEmpty()) list.add(Condition.OK)
        condition = list
    }

    fun getInfoToChange(): String {
        if (limitDays == 0) return "${getMileageToRepair()} km"
        if (limitKM == 0)   return "${getDaysToRepair()} days"
        return "${getMileageToRepair()} km/${getDaysToRepair()} days"
    }

    fun getLineForBuyList(): String {
        return if(isNeedToBuy()){
            " $name: $codes"
        }else{
            ""
        }
    }

    fun getLineForService(): String {
        if(type == PartControlType.INSPECTION){
            if (isOverRide())
                return " Make inspection for $name"
        }else{
            if (isOverRide())
                return " !!!ATTENTION!!! $name  OVERUSED ${checkDayOrKm(0, 0)}"
            if (isNeedToService()) {
                return " Make service for $name, do service in ${checkDayOrKm(warnDayToService, warnKMToService)}"
            }
            if (isNeedToBuy() && limitKM == 0)
                return " Ready to continue $name, left ${getDaysToRepair()} day(s)"
            if (isNeedToBuy())
                return " Buy $name, left ${checkDayOrKm(warnDayToBuy, warnKMToBuy)}"
        }
        return ""
    }

    fun makeService(): Repair {
        mileageLastChange = mileage

        dateLastChange = if (type == PartControlType.INSURANCE) dateLastChange.plusDays(
            insurancePeriod.toLong()
        )
        else LocalDate.now()

        val repair = Repair()
        repair.type = "change"
        repair.mileage = mileage
        repair.carId = carId
        repair.partId = id
        repair.description = "$name is changed during technical works: $codes"
        repair.date = LocalDate.now()
        if (type == PartControlType.INSURANCE)
            repair.description = "auto prolonged insurance for: ${dateLastChange.plusDays(
                insurancePeriod.toLong()//? check it on preference correct working
            )}"
        condition = refreshCondition()
        return repair
    }

    private fun refreshCondition(): List<Condition>{
        val list = mutableListOf<Condition>()
        if (isNeedToInspection()) list.add(Condition.MAKE_INSPECTION)
        if (isNeedToBuy()) list.add(Condition.BUY_PARTS)
        if (isNeedToService()) list.add(Condition.MAKE_SERVICE)
        if (isOverRide()) list.add(Condition.ATTENTION)
        if (list.isEmpty()) list.add(Condition.OK)
        return list
    }

    private fun getUsedMileage() = mileage - mileageLastChange

    private fun isNeedToBuy(): Boolean {
        if (type == PartControlType.INSPECTION) return false
        if (getMileageToRepairForDayOrKmIsNull() < warnKMToBuy ||
            getDaysToRepairForDayOrKmIsNull() < warnDayToBuy) return true
        return false
    }

    private fun isNeedToService(): Boolean {
        if (type == PartControlType.INSPECTION) return false
        if (getDaysToRepairForDayOrKmIsNull() < warnDayToService ||
            getMileageToRepairForDayOrKmIsNull() < warnKMToService) return true
        return false
    }

    private fun isNeedToInspection(): Boolean {
        if ((getDaysToRepairForDayOrKmIsNull() < 0 || getMileageToRepairForDayOrKmIsNull() < 0) &&
            type == PartControlType.INSPECTION)
            return true
        return false
    }

    private fun isOverRide(): Boolean {
        if (type == PartControlType.INSPECTION) return false
        if (getDaysToRepairForDayOrKmIsNull() < 0 || getMileageToRepairForDayOrKmIsNull() < 0) return true
        return false
    }

    private fun getDaysToRepair() = limitDays - ChronoUnit.DAYS.between(dateLastChange, LocalDate.now()).toInt()

    private fun getMileageToRepair() = limitKM - getUsedMileage()

    private fun checkDayOrKm(warnDays: Int, warnKm: Int): String{
        if (getDaysToRepairForDayOrKmIsNull() < warnDays && getMileageToRepairForDayOrKmIsNull() < warnKm)
            return "${getDaysToRepair()} day(s)/${getMileageToRepair()} km"
        if (getDaysToRepairForDayOrKmIsNull() < warnDays)
            return "${getDaysToRepair()} day(s)"
        if (getMileageToRepairForDayOrKmIsNull() < warnKm)
            return "${getMileageToRepair()} km"
        return "error"
    }

    private fun getDaysToRepairForDayOrKmIsNull(): Int {
        return if (limitDays == 0) 100
        else limitDays - ChronoUnit.DAYS.between(dateLastChange, LocalDate.now()).toInt()
    }

    private fun getMileageToRepairForDayOrKmIsNull(): Int {
        return if (limitKM == 0) 10000
        else limitKM - getUsedMileage()
    }
}