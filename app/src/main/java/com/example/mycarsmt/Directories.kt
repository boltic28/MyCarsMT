package com.example.mycarsmt

import android.annotation.SuppressLint

enum class Directories(val value: String) {
    @SuppressLint("SdCardPath")
    PART_IMAGE_DIRECTORY("/sdcard/MyCarsMT/images/parts/"),
    @SuppressLint("SdCardPath")
    CAR_IMAGE_DIRECTORY("/sdcard/MyCarsMT/images/cars/"),
}