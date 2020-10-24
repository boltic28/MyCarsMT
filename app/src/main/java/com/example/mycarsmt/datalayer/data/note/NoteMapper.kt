package com.example.mycarsmt.datalayer.data.note

import com.example.mycarsmt.businesslayer.Note

fun getEntityFrom(note: Note): NoteEntity =
    NoteEntity(
        id = note.id,
        carId = note.carId,
        partId = note.partId,
        description = note.description,
        date = note.date,
        importantLevel = note.importantLevel
    )

fun getNoteFromEntity(note: NoteWithMileage): Note =
    Note(
        id = note.id,
        carId = note.carId,
        partId = note.partId,
        description = note.description,
        mileage = note.mileage,
        date = note.date,
        importantLevel = note.importantLevel
    )