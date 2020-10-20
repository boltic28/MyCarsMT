package com.example.mycarsmt

import com.example.mycarsmt.datalayer.data.car.CarEntity
import com.example.mycarsmt.datalayer.data.car.CarWithAllElements

class CarTestingHelper {
    fun compareCars(car1: CarEntity, car2: CarWithAllElements): Boolean {
        if (car1.brand == car2.brand &&
            car1.model == car2.model &&
            car1.mileage == car2.mileage &&
            car1.number == car2.number &&
            car1.vin == car2.vin &&
            car1.condition == car2.condition &&
            car1.year == car2.year &&
            car1.whenMileageRefreshed == car2.whenMileageRefreshed &&
            car2.partEntities != null && car2.noteEntities != null && car2.repairEntities != null
        ) return true
        return false
    }

    fun compareCars(car1: CarEntity, car2: CarEntity): Boolean {
        if (car1.brand == car2.brand &&
            car1.model == car2.model &&
            car1.mileage == car2.mileage &&
            car1.number == car2.number &&
            car1.vin == car2.vin &&
            car1.condition == car2.condition &&
            car1.year == car2.year &&
            car1.whenMileageRefreshed == car2.whenMileageRefreshed
        ) return true
        return false
    }

}