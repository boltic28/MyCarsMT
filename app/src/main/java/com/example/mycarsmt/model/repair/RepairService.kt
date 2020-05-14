package com.example.mycarsmt.model.repair

interface RepairService{

    fun create()
    fun read()
    fun update()
    fun delete()

    fun toStringForDB(): String
    fun toStringForCarHistory(): String
}