package com.example.mycarsmt.model.database.part

import androidx.room.ColumnInfo
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.mycarsmt.model.enums.PartControlType
import com.example.mycarsmt.model.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.model.database.vconvertors.TypeControlConverter
import com.example.mycarsmt.model.database.note.NoteEntity
import com.example.mycarsmt.model.database.repair.RepairEntity
import java.time.LocalDate

class PartWithMileageAndElements {

    var id: Long = 0

    @ColumnInfo(name = "car_id")
    var carId: Long = 0

    var mileage: Int = 0
    var name: String = ""
    var codes: String = ""

    @ColumnInfo(name = "limit_km")
    var limitKM: Int = 0

    @ColumnInfo(name = "limit_day")
    var limitDays: Int = 0

    @ColumnInfo(name = "last_change_time")
    @TypeConverters(LocalDateConverter::class)
    var dateLastChange: LocalDate = LocalDate.now()

    @ColumnInfo(name = "last_change_km")
    var mileageLastChange: Int = 0

    var description: String = ""
    var photo = ""

    @TypeConverters(TypeControlConverter::class)
    var type: PartControlType = PartControlType.CHANGE

    @Relation(parentColumn = "id", entityColumn = "part_id")
    var noteEntities: List<NoteEntity> = emptyList()

    @Relation(parentColumn = "id", entityColumn = "part_id")
    var repairEntities: List<RepairEntity> = emptyList()
}