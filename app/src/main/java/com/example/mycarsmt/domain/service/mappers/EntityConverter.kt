package com.example.mycarsmt.domain.service.mappers

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.data.database.car.CarEntity
import com.example.mycarsmt.data.database.note.NoteEntity
import com.example.mycarsmt.data.database.note.NoteWithMileage
import com.example.mycarsmt.data.database.part.PartEntity
import com.example.mycarsmt.data.database.part.PartWithMileage
import com.example.mycarsmt.data.database.repair.RepairEntity

class EntityConverter {
    companion object {
        fun carEntityFrom(car: Car): CarEntity {
            return CarEntity(
                car.id, car.brand, car.model, car.number, car.vin,
                car.photo, car.year, car.mileage, car.whenMileageRefreshed, car.condition
            )
        }

        fun carFrom(car: CarEntity): Car {
            return Car(
                car.id, car.brand, car.model, car.number, car.vin,
                car.photo, car.year, car.mileage, car.whenMileageRefreshed, car.condition
            )
        }

        fun partEntityFrom(part: Part): PartEntity {
            return PartEntity(
                part.id, part.carId, part.name, part.codes, part.limitKM, part.limitDays,
                part.dateLastChange, part.mileageLastChange, part.description, part.photo,
                part.type, part.condition
            )
        }

        fun partFrom(part: PartEntity): Part {
            return Part(
                part.id, part.carId, part.name, part.codes, part.limitKM, part.limitDays,
                part.dateLastChange, part.mileageLastChange, part.description, part.photo,
                part.type, part.condition
            )
        }

        fun partFrom(part: PartWithMileage): Part {
            return Part(
                part.id, part.carId, part.mileage,
                part.name, part.codes, part.limitKM, part.limitDays, part.dateLastChange,
                part.mileageLastChange, part.description, part.photo, part.type, part.condition
            )
        }

        fun noteEntityFrom(note: Note): NoteEntity {
            return NoteEntity(
                note.id, note.carId, note.partId,
                note.description, note.date, note.importantLevel
            )
        }

        fun noteFrom(note: NoteEntity): Note {
            return Note(
                note.id, note.carId, note.partId,
                note.description, note.date, note.importantLevel
            )
        }

        fun noteFrom(note: NoteWithMileage): Note {
            return Note(
                note.id, note.carId, note.mileage, note.partId,
                note.description, note.date, note.importantLevel
            )
        }

        fun repairEntityFrom(repair: Repair): RepairEntity {
            return RepairEntity(
                repair.id, repair.carId, repair.partId, repair.type, repair.cost, repair.mileage,
                repair.description, repair.date
            )
        }

        fun repairFrom(repair: RepairEntity): Repair {
            return Repair(
                repair.id, repair.carId, repair.partId, repair.type, repair.cost, repair.mileage,
                repair.description, repair.date
            )
        }

    }
}