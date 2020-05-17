package com.example.mycarsmt.model.repo.note

import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Note

interface NoteService{

    fun create(note: Note): Long
    fun update(note: Note): Long
    fun delete(note: Note): Long
    fun readById(id: Long): LiveData<Note>
    fun readAll(): LiveData<List<Note>>

//    fun checkImportantLevel(imageView: ImageView)
//    fun isHighImportant(): Boolean
//    fun done()
}