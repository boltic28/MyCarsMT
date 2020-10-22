package com.example.mycarsmt.datalayer.data.part

import androidx.room.*
import com.example.mycarsmt.datalayer.enums.PartControlType
import com.example.mycarsmt.datalayer.data.car.CarEntity
import com.example.mycarsmt.datalayer.enums.Condition
import java.time.LocalDate

@Entity(
    tableName = "part", foreignKeys = [
        ForeignKey(
            entity = CarEntity::class, parentColumns = ["id"], childColumns = ["car_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class PartEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "car_id")
    val carId: Long,
    val name: String,
    val codes: String,
    val limitKM: Int,
    val limitDays: Int,
    val dateLastChange: LocalDate,
    val mileageLastChange: Int,
    val description: String,
    val photo: String,
    val type: PartControlType = PartControlType.CHANGE,
    val condition: List<Condition> = listOf(Condition.OK)
)