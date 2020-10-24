package com.example.mycarsmt.datalayer.data.part

import com.example.mycarsmt.businesslayer.Part

fun getEntityFrom(part: Part): PartEntity =
    PartEntity(
        id = part.id,
        carId = part.carId,
        name = part.name,
        codes = part.codes,
        limitKM = part.limitKM,
        limitDays = part.limitDays,
        dateLastChange = part.dateLastChange,
        mileageLastChange = part.mileageLastChange,
        description = part.description,
        photo = part.photo,
        type = part.type,
        condition = part.condition
    )

fun getPartFrom(part: PartWithMileage): Part =
    Part(
        id = part.id,
        carId = part.carId,
        name = part.name,
        codes = part.codes,
        limitKM = part.limitKM,
        limitDays = part.limitDays,
        dateLastChange = part.dateLastChange,
        mileageLastChange = part.mileageLastChange,
        description = part.description,
        photo = part.photo,
        type = part.type,
        condition = part.condition
    )