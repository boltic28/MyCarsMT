package com.example.mycarsmt.model.part

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.example.mycarsmt.model.repo.part.Part
import com.example.mycarsmt.model.repo.repair.Repair

@SuppressLint("NewApi")
class PartServiceImpl(val context: Context, val part: Part): PartService {
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

    override fun getMileageToRepair(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUsedMileage(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDaysToRepair(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhotoFor(imageView: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfoToChange(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLineForBuyList(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRepairs(): List<Repair> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNeedToBuy(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNeedToService(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNeedToInspection(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isOverRide(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun makeService() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLineForService(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getConditionImageFor(imageView: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toStringForDB(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}