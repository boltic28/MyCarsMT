package com.example.mycarsmt.data.database.note

import androidx.room.*
import com.example.mycarsmt.data.enums.NoteLevel
import com.example.mycarsmt.data.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.data.database.car.CarEntity
import com.example.mycarsmt.data.database.vconvertors.NoteLevelConverter
import java.time.LocalDate

@Entity(tableName = "note", foreignKeys = [
    ForeignKey(entity = CarEntity::class, parentColumns = ["id"], childColumns = ["car_id"],
        onDelete = ForeignKey.CASCADE)
])
class NoteEntity() {

    constructor(
        id: Long,
        carId: Long,
        partId: Long,
        description: String,
        date: LocalDate,
        importantLevel: NoteLevel
    ) : this() {
        this.id = id
        this.carId = carId
        this.partId = partId
        this.description = description
        this.date = date
        this.importantLevel = importantLevel
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "car_id")
    var carId: Long = 0

    @ColumnInfo(name = "part_id")
    var partId: Long = 0

    var description: String = "some note"

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate = LocalDate.now()

    @ColumnInfo(name = "important_level")
    @TypeConverters(NoteLevelConverter::class)
    var importantLevel: NoteLevel = NoteLevel.INFO
}