package com.example.mycarsmt.datalayer.data.car

import com.example.mycarsmt.businesslayer.Car

fun getEntityFrom(car: Car): CarEntity =
    CarEntity(
        id = car.id,
        brand = car.brand,
        model = car.model,
        number = car.number,
        vin = car.vin,
        photo = car.photo,
        year = car.year,
        mileage = car.mileage,
        whenMileageRefreshed = car.whenMileageRefreshed,
        condition = car.condition
    )

fun getCarFrom(entity: CarEntity): Car =
    Car(
        id = entity.id,
        brand = entity.brand,
        model = entity.model,
        number = entity.number,
        vin = entity.vin,
        photo = entity.photo,
        year = entity.year,
        mileage = entity.mileage,
        whenMileageRefreshed = entity.whenMileageRefreshed,
        condition = entity.condition
    )