package com.example.mycarsmt.model.repo.repair

import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Repair

interface RepairService{

    fun create(repair: Repair): Long
    fun update(repair: Repair): Int
    fun delete(repair: Repair): Int
    fun readAll(): LiveData<List<Repair>>
    fun readById(id: Long): LiveData<Repair>

//    fun toStringForCarHistory(): String
}