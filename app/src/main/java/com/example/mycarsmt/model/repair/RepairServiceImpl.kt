package com.example.mycarsmt.model.repair

import android.annotation.SuppressLint
import android.content.Context
import com.example.mycarsmt.model.repo.repair.Repair

@SuppressLint("NewApi")
class RepairServiceImpl(var context: Context, val repair: Repair): RepairService {
    override fun create() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toStringForDB(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toStringForCarHistory(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}