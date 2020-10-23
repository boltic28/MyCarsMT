package com.example.mycarsmt.datalayer.data.repair

import androidx.room.*
import com.example.mycarsmt.datalayer.data.car.CarEntity
import java.time.LocalDate

@Entity(
    tableName = "repair", foreignKeys = [
        ForeignKey(
            entity = CarEntity::class, parentColumns = ["id"], childColumns = ["car_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RepairEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "car_id")
    val carId: Long,

    @ColumnInfo(name = "part_id")
    val partId: Long,
    val type: String,
    val cost: Int,
    val mileage: Int,
    val description: String,
    val date: LocalDate = LocalDate.now()
)