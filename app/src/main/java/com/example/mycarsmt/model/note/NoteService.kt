package com.example.mycarsmt.model.note

import android.widget.ImageView

interface NoteService{

    fun create()
    fun read()
    fun update()
    fun delete()

    fun checkImportantLevel(imageView: ImageView)
    fun doneIt()

    fun toStringForDB(): String
}