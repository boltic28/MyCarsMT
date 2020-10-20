package com.example.mycarsmt.datalayer.data.note

import androidx.room.*
import com.example.mycarsmt.datalayer.enums.NoteLevel
import com.example.mycarsmt.datalayer.vconvertors.LocalDateConverter
import com.example.mycarsmt.datalayer.data.car.CarEntity
import com.example.mycarsmt.datalayer.vconvertors.NoteLevelConverter
import java.time.LocalDate

@Entity(
    tableName = "note", foreignKeys = [
        ForeignKey(
            entity = CarEntity::class, parentColumns = ["id"], childColumns = ["car_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "car_id")
    val carId: Long,

    @ColumnInfo(name = "part_id")
    val partId: Long,

    var description: String,

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate,

    @ColumnInfo(name = "important_level")
    @TypeConverters(NoteLevelConverter::class)
    var importantLevel: NoteLevel
)
