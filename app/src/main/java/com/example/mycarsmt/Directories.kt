package com.example.mycarsmt

import android.annotation.SuppressLint

enum class Directories(val value: String) {
    @SuppressLint("SdCardPath")
    PART_IMAGE_DIRECTORY("/sdcard/MyCarsMT/images/parts/"),
    @SuppressLint("SdCardPath")
    CAR_IMAGE_DIRECTORY("/sdcard/MyCarsMT/images/cars/"),

    @SuppressLint("SdCardPath")
    CARS_HISTORY("/sdcard/MyCars/History/"),
    @SuppressLint("SdCardPath")
    CARS_RIP_FILE("/sdcard/MyCars/DeletedCars/"),

    @SuppressLint("SdCardPath")
    COMMON_DATA_DIRECTORY("/sdcard/MyCarMT/Common/"),
    @SuppressLint("SdCardPath")
    DATA_OUTPUT_FILE(COMMON_DATA_DIRECTORY.value + "carsData.txt"),
    @SuppressLint("SdCardPath")
    TO_DO_OUTPUT_FILE(COMMON_DATA_DIRECTORY.value + "toDo.txt"),
    @SuppressLint("SdCardPath")
    TO_BUY_OUTPUT_FILE(COMMON_DATA_DIRECTORY.value + "toBuy.txt"),
    @SuppressLint("SdCardPath")
    TO_NOTES_OUTPUT_FILE(COMMON_DATA_DIRECTORY.value + "notes.txt"),

    @SuppressLint("SdCardPath")
    TXT_FILE_WITH_CARS("/sdcard/Download/cars.txt"),
    @SuppressLint("SdCardPath")
    TXT_OUTPUT_FILE("/sdcard/Download/cars.txt")
}