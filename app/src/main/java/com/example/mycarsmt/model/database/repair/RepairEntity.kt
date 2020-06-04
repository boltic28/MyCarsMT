package com.example.mycarsmt.model.database.repair

import androidx.room.*
import com.example.mycarsmt.model.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.model.database.car.CarEntity
import com.example.mycarsmt.model.database.part.PartEntity
import java.time.LocalDate

@Entity(tableName = "repair", foreignKeys = [
ForeignKey(entity = CarEntity::class, parentColumns = ["id"], childColumns = ["car_id"],
    onDelete = ForeignKey.CASCADE)
])
class RepairEntity() {

    constructor(
        id: Long,
        carId: Long,
        partId: Long,
        type: String,
        cost: Int,
        mileage: Int,
        description: String,
        date: LocalDate
    ) : this() {
        this.id = id
        this.carId = carId
        this.partId = partId
        this.type = type
        this.cost = cost
        this.mileage = mileage
        this.description = description
        this.date = date
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "car_id")
    var carId: Long = 0

    @ColumnInfo(name = "part_id")
    var partId: Long = 0

    var type: String = "type"

    var cost: Int = 0

    var mileage: Int = 0

    var description: String = "some repair"

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate = LocalDate.now()
}