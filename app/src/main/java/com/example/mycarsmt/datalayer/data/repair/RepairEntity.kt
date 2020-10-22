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
    var id: Long,

    @ColumnInfo(name = "car_id")
    var carId: Long,

    @ColumnInfo(name = "part_id")
    var partId: Long,
    var type: String,
    var cost: Int,
    var mileage: Int,
    var description: String,
    var date: LocalDate = LocalDate.now()
)