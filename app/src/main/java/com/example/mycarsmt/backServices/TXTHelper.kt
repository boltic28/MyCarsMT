package com.example.mycarsmt.backServices

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.Directories
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.enums.NoteLevel
import com.example.mycarsmt.datalayer.enums.PartControlType
import com.example.mycarsmt.businesslayer.*
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.businesslayer.service.note.NoteRepositoryImpl
import com.example.mycarsmt.businesslayer.service.part.PartServiceImpl
import com.example.mycarsmt.businesslayer.service.repair.RepairServiceImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class TXTConverter @Inject constructor() {

    companion object {
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var noteService: NoteRepositoryImpl
    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var repairService: RepairServiceImpl
    @Inject
    lateinit var preferences: SharedPreferences

    init {
        App.component.injectTXTConverter(this)
    }

    private val dateMapper = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)!!

    fun getFileWithListToDo(list: List<DiagnosticElement>): File {
        testCommonDirectory()
        var result = ""
        list.forEach { event ->
            result += "${event.car.getFullName()}\n"
            event.list.forEach { job ->
                result += "$job\n"
            }
        }
        val file = File(Directories.TO_DO_OUTPUT_FILE.value)
        file.createNewFile()
        file.writeText(result.substring(0, result.length - 1))
        return file
    }

    fun getFileWithListToBuy(list: List<DiagnosticElement>): File {
        testCommonDirectory()
        var result = ""
        list.forEach { event ->
            result += "${event.car.getFullName()}\n"
            event.list.forEach { job ->
                result += "$job\n"
            }
        }
        val file = File(Directories.TO_BUY_OUTPUT_FILE.value)
        file.createNewFile()
        file.writeText(result.substring(0, result.length - 1))
        return file
    }

    fun writeFileWithNotes(cars: List<Car>): File {
        testCommonDirectory()
        val file = File(Directories.TO_NOTES_OUTPUT_FILE.value)
        file.createNewFile()
        var result = ""
        cars.forEach { car ->
            val notes = car.notes
            if (notes.isNotEmpty()) {
                result = result.plus("${car.getFullName()} + \n")
                notes.forEach { note ->
                    result = result.plus(getDataForFile(note))
                }
            }
        }

        file.writeText(result.substring(0, result.length - 1))
        return file
    }

    fun writeCarHistoryToFile(car: Car): String {
        val fileDirectory = File(Directories.CARS_HISTORY.value)
        if (!fileDirectory.exists()) fileDirectory.mkdirs()

        val filePath =
            Directories.CARS_HISTORY.value + "${car.brand}_${car.model}(${car.number}).txt"
        File(filePath).createNewFile()
        var result = "${car.getFullName()} ${car.year} ${car.mileage}km ${LocalDate.now()}\n"
        car.repairs.forEach { result = result.plus(getLineForHistoryFrom(it)) }
        File(filePath).writeText(result.substring(0, result.length - 1))
        return "car's history was write to file $filePath"
    }

    fun writeFileWithCarsData(cars: List<Car>): File {
        testCommonDirectory()
        val file = File(Directories.DATA_OUTPUT_FILE.value)
        file.createNewFile()
        var result = ""
        cars.forEach { car ->
            result = result.plus(getDataForMileageList(car))
        }

        file.writeText(result.substring(0, result.length - 1))
        return file
    }


    private fun getLineForHistoryFrom(repair: Repair): String {
        return "${repair.date} ${repair.mileage} km: \n" +
                "\t $${repair.cost}, ${repair.type} - ${repair.description} \n"
    }

    fun createCarCopyToFile(car: Car) {
        val fileDirectory = File(Directories.CARS_RIP_FILE.value)
        if (!fileDirectory.exists()) fileDirectory.mkdirs()

        val filePath = Directories.CARS_RIP_FILE.value + "${car.model}(${car.number}).txt"
        File(filePath).createNewFile()
        val result = toStringForDB(car)
        File(filePath).writeText(result.substring(0, result.length - 1))
    }

    @SuppressLint("CheckResult")
    fun writeCarsToFile(): Single<String> {
        File(Directories.TXT_OUTPUT_FILE.value).createNewFile()
        var result = ""
        carService.preparingCarsToWritingIntoFile()
            .subscribeOn(Schedulers.io())
            .subscribe { cars ->
                cars.forEach { car ->
                    result = result.plus(toStringForDB(car))
                }

                File(Directories.TXT_OUTPUT_FILE.value).writeText(
                    result.substring(
                        0,
                        result.length - 1
                    )
                )
            }
        return Single.just("Cars saved in Download/cars.txt")
    }

    private fun toStringForDB(car: Car): String {

        var line = "c ${car.brand},${car.model},${car.year},${car.vin},${car.number}," +
                "${car.mileage},${car.photo}\n"

        car.notes.stream()
            .filter { it.partId == 0L }
            .forEach { line = line.plus(toStringForDB(it)) }

        car.repairs.stream()
            .filter { it.partId == 0L }
            .forEach { line = line.plus(toStringForDB(it)) }

        car.parts.forEach { part ->
            line = line.plus(toStringForDB(part))
            part.notes.forEach { line = line.plus(toStringForDB(it)) }
            part.repairs.forEach { line = line.plus(toStringForDB(it)) }
        }

        return line
    }

    private fun toStringForDB(note: Note): String {
        return "n ${note.description.replace(',', ';')}" +
                ",${note.importantLevel.value},${note.date}\n"
    }

    private fun toStringForDB(part: Part): String {
        return "p ${part.name},${part.codes.replace(',', ':')}" +
                ",${part.limitKM},${part.limitDays},${part.dateLastChange},${part.mileageLastChange}" +
                ",${part.description.replace(',', ';')},${part.photo},${part.type.value}\n"
    }

    private fun toStringForDB(repair: Repair): String {
        return "r ${repair.type},${repair.description.replace(',', ';')},${repair.mileage},${repair.date}," +
                "${repair.cost}\n"

    }


    fun readDataFromFile(): Single<Unit> {

        val f = File(Directories.TXT_FILE_WITH_CARS.value)
        val inputStream: InputStream = f.inputStream()
        val cars: MutableList<Car> = mutableListOf()
        var parts: MutableList<Part> = mutableListOf()
        var notes: MutableList<Note> = mutableListOf()
        var repairs: MutableList<Repair> = mutableListOf()
        var counterCars = 0
        var counterParts = 0
        var counterNotes = 0
        var counterRepairs = 0
        var tmpCar: Car? = null
        var tmpPart: Part? = null

        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { lineFromFile ->
                val objectType = lineFromFile[0]
                val line = lineFromFile.removeRange(0, 2)
                Log.d(TAG, "Reading line: $line")
                when (objectType) {
                    'c' -> {
                        if (cars.isNotEmpty()) {
                            tmpCar!!.parts = parts

                            if (parts.isEmpty()) {
                                tmpCar!!.notes = notes
                                tmpCar!!.repairs = repairs

                            } else {
                                tmpPart!!.notes = notes
                                tmpPart!!.repairs = repairs
                            }
                            notes = mutableListOf()
                            repairs = mutableListOf()
                            parts = mutableListOf()
                        }

                        tmpCar = createCarFromLine(line)
                        cars.add(tmpCar!!)
                        counterCars++
                        Log.d(TAG, "Reading car: ${tmpCar?.getFullName()}")
                    }
                    'p' -> {
                        if (parts.isEmpty()) {
                            tmpCar!!.notes = notes
                            tmpCar!!.repairs = repairs

                        } else {
                            tmpPart!!.notes = notes
                            tmpPart!!.repairs = repairs
                        }
                        notes = mutableListOf()
                        repairs = mutableListOf()


                        tmpPart = createPartFromLine(line)
                        parts.add(tmpPart!!)
                        counterParts++
                        Log.d(TAG, "Reading part: ${tmpPart?.name}")
                    }
                    'r' -> {
                        repairs.add(createRepairFromLine(line))
                        counterRepairs++
                        Log.d(TAG, "Reading repair...")
                    }
                    'n' -> {
                        notes.add(createNoteFromLine(line))
                        counterNotes++
                        Log.d(TAG, "Reading note...")
                    }
                }
            }
            // writing the last data to the car
            if (cars.isNotEmpty()) {
                tmpCar!!.parts = parts

                if (parts.isEmpty()) {
                    tmpCar!!.notes = notes
                    tmpCar!!.repairs = repairs

                } else {
                    tmpPart!!.notes = notes
                    tmpPart!!.repairs = repairs
                }
                notes = mutableListOf()
                repairs = mutableListOf()
                parts = mutableListOf()
            }
        }

        return carService.createCarsFromFile(cars)
    }

    private fun getDataForFile(repair: Note): String {
        return "\t -> ${repair.description}\n"
    }

    private fun getDataForMileageList(car: Car): String {
        var result = "${car.year}: ${car.brand} ${car.model} "
        while (result.length < 25) result += " "
        result += " | ${car.number} | - mileage: ${car.mileage} km\n"

        return result
    }

    private fun testCommonDirectory() {
        val file = File(Directories.COMMON_DATA_DIRECTORY.value)
        if (!file.exists())
            file.mkdirs()
    }

    @SuppressLint("CheckResult")
    private fun createCarFromLine(line: String): Car {

        // brand[0], model[1], year[2], win[3], numb[4], odo[5], photo[6] - > type of line from cars.txt

        val sa = line.split(",")

        val car = Car()
        car.brand = sa[0]
        car.model = sa[1]
        car.vin = sa[3]
        car.number = sa[4]
        try {
            car.mileage = sa[5].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading car: ${car.number} - problem with mileage \n" +
                    " $line")
        }
        try {
            car.year = sa[2].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading car: ${car.number} - problem with year \n $line")
        }
        car.photo = sa[6]

        return car
    }

    @SuppressLint("CheckResult")
    private fun createPartFromLine(line: String): Part {

        // name[0], codes[1], limitKM[2], limitDay[3], dateInstall[4], milleageInstal[5], description[6], photo[7], type[8]

        val sa = line.split(",")

        val part = Part()

        part.name = sa[0]
        part.codes = sa[1].replace(':', ',')
        try {
            part.limitKM = sa[2].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading part: ${part.name} - problem with limitkm - ${sa[2]} \n" +
                    " $line")
        }
        try {
            part.limitDays = sa[3].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading part: ${part.name} - problem with limitDay - ${sa[3]} \n" +
                    " $line")
        }
        try {
            part.dateLastChange = LocalDate.parse(sa[4], dateMapper)
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Reading part: ${part.name} - problem with date last change - ${sa[4]} \n" +
                        " $line"
            )
        }
        try {
            part.mileageLastChange = sa[5].toInt()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Reading part: ${part.name} - problem with mileage last change - ${sa[5]} \n" +
                        " $line"
            )
        }
        part.description = sa[6].replace(';', ',')
        part.photo = sa[7]
        part.type = PartControlType.fromString(sa[8])!!

        return part
    }

    @SuppressLint("CheckResult")
    private fun createRepairFromLine(line: String): Repair {

        // type[0], description[1], mileage[2], dateInstall[3], cost[4]
        val sa = line.split(",")

        val repair = Repair()

        repair.type = sa[0]
        repair.description = sa[1].replace(';', ',')
        try {
            repair.cost = sa[4].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading repair: ${repair.type} - problem with cost - ${sa[5]} \n" +
                    " $line")
        }
        try {
            repair.mileage = sa[2].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading repair: ${repair.type} - problem with mileage - ${sa[2]} \n" +
                    " $line")
        }
        try {
            repair.date = LocalDate.parse(sa[3], dateMapper)
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Reading repair: ${repair.type} - problem with date change - ${sa[3]} \n" +
                        " $line"
            )
        }

        return repair
    }

    @SuppressLint("CheckResult")
    private fun createNoteFromLine(line: String): Note {

        // description[0], importantLevel[1], dateInstall[2]
        val sa = line.split(",")

        val note = Note()

        note.description = sa[0].replace(';', ',')
        try {
            note.importantLevel = NoteLevel.fromInt(sa[1].toInt())!!
        } catch (e: Exception) {
            note.importantLevel = NoteLevel.MIDDLE
            Log.d(TAG, "Reading note: ${note.importantLevel} - problem with date - ${sa[1]} \n" +
                    " $line")
        }

        try {
            note.date = LocalDate.parse(sa[2], dateMapper)
        } catch (e: Exception) {
            Log.d(TAG, "Reading note: ${note.date} - problem with date - ${sa[2]} \n" +
                    " $line")
        }

        return note
    }

}