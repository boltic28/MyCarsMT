package com.example.mycarsmt.model.database.car

import androidx.room.ColumnInfo
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.mycarsmt.model.enums.CarCondition
import com.example.mycarsmt.model.database.vconvertors.ConditionConverter
import com.example.mycarsmt.model.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.model.database.note.NoteEntity
import com.example.mycarsmt.model.database.part.PartEntity
import com.example.mycarsmt.model.database.repair.RepairEntity
import java.time.LocalDate

class CarWithAllElements {

    var id: Long = 0

    var brand: String = ""
    var model: String = ""
    var number: String = ""
    var vin: String = ""
    var photo: String = ""

    var year: Int = 1980
    var mileage: Int = 0

    @ColumnInfo(name = "mileage_changed_at")
    @TypeConverters(LocalDateConverter::class)
    var whenMileageRefreshed: LocalDate = LocalDate.now()

    @TypeConverters(ConditionConverter::class)
    var condition: CarCondition = CarCondition.OK

    @Relation(parentColumn = "id", entityColumn = "car_id")
    var partEntities: List<PartEntity> = emptyList()

    @Relation(parentColumn = "id", entityColumn = "car_id")
    var noteEntities: List<NoteEntity> = emptyList()

    @Relation(parentColumn = "id", entityColumn = "car_id")
    var repairEntities: List<RepairEntity> = emptyList()
}