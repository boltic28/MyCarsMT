package com.example.mycarsmt.datalayer.data.repair

import com.example.mycarsmt.businesslayer.Repair

fun getEntityFrom(repair: Repair): RepairEntity =
    RepairEntity(
        id = repair.id,
        carId = repair.id,
        partId = repair.partId,
        type = repair.type,
        cost = repair.cost,
        mileage = repair.mileage,
        description = repair.description,
        date = repair.date
    )

fun getRepairFrom(entity: RepairEntity): Repair =
    Repair(
        id = entity.id,
        carId = entity.id,
        partId = entity.partId,
        type = entity.type,
        cost = entity.cost,
        mileage = entity.mileage,
        description = entity.description,
        date = entity.date
    )