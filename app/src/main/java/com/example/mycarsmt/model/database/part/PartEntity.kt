package com.example.mycarsmt.model.database.part

import androidx.room.*
import com.example.mycarsmt.model.enums.PartControlType
import com.example.mycarsmt.model.database.car.CarEntity
import com.example.mycarsmt.model.database.vconvertors.ConditionConverter
import com.example.mycarsmt.model.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.model.database.vconvertors.TypeControlConverter
import com.example.mycarsmt.model.enums.Condition
import java.time.LocalDate

@Entity(tableName = "part", foreignKeys = [
    ForeignKey(entity = CarEntity::class, parentColumns = ["id"], childColumns = ["car_id"],
        onDelete = ForeignKey.CASCADE)
])
class PartEntity() {

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

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "car_id")
    var carId: Long = 0

    var name: String = "part"
    var codes: String = "no23"

    @ColumnInfo(name = "limit_km")
    var limitKM: Int = 10000

    @ColumnInfo(name = "limit_day")
    var limitDays: Int = 365

    @ColumnInfo(name = "last_change_time")
    @TypeConverters(LocalDateConverter::class)
    var dateLastChange: LocalDate = LocalDate.now()

    @ColumnInfo(name = "last_change_km")
    var mileageLastChange: Int = 0

    var description: String = "description"
    var photo = ""

    @TypeConverters(TypeControlConverter::class)
    var type: PartControlType = PartControlType.CHANGE

    @TypeConverters(ConditionConverter::class)
    var condition: List<Condition> = listOf(Condition.OK)
}