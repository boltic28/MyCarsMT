package com.example.mycarsmt.backServices

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.Directories
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.NoteLevel
import com.example.mycarsmt.domain.*
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
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
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var noteService: NoteServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var repairService: RepairServiceImpl
    @Inject
    lateinit var preferences: SharedPreferences

    init {
        App.component.injectTXTConverter(this)
    }


    private val dateMapper = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)!!

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
//            getListOfNotes().forEach {
//                result = result.plus({
//                    if (it.ownerId == 0)
//                        it.getDataForFile()
//                }
//                )
//            }

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
        var counter = 0
        carService.getAll()
            .subscribeOn(Schedulers.io())
            .subscribe { cars ->
                cars.forEach { car ->
                    noteService.getAllForCar(car)
                        .subscribeOn(Schedulers.io())
                        .subscribe { notes -> car.notes = notes }
                    repairService.getAllForCar(car)
                        .subscribeOn(Schedulers.io())
                        .subscribe { repairs -> car.repairs = repairs }
                    partService.getAllForCar(car)
                        .subscribeOn(Schedulers.io())
                        .subscribe { parts ->
                            car.parts = parts
                            parts.forEach { part ->
                                noteService.getAllForPart(part)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { notes -> part.notes = notes }
                                repairService.getAllForPart(part)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { repairs -> part.repairs = repairs }
                            }
                        }
                }
                cars.forEach { car ->
                    result = result.plus(toStringForDB(car))
                    counter++
                }

                File(Directories.TXT_OUTPUT_FILE.value).writeText(
                    result.substring(
                        0,
                        result.length - 1
                    )
                )
            }

        return Single.just("$counter cars written to file ${Directories.TXT_OUTPUT_FILE.value}")
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
        return "n ${note.description},${note.importantLevel},${note.date}\n"
    }

    private fun toStringForDB(part: Part): String {
        return "p ${part.name},${part.codes.replace(',', ':')}" +
                ",${part.limitKM},${part.limitDays},${part.dateLastChange},${part.mileageLastChange}" +
                ",${part.description},${part.photo}\n"
    }

    private fun toStringForDB(repair: Repair): String {
        return "r ${repair.type},${repair.description},${repair.mileage},${repair.date}," +
                "${repair.cost}\n"

    }


    fun readDataFromFile(): Single<String> {

        val f = File(Directories.TXT_FILE_WITH_CARS.value)
        val inputStream: InputStream = f.inputStream()
        val cars : MutableList<Car>
        var counterCars = 0
        var counterParts = 0
        var counterNotes = 0
        var counterRepairs = 0
        lateinit var tmpCar: Car
        lateinit var tmpPart: Part

        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { lineFromFile ->
                Log.d(TAG, "Reading file: $lineFromFile")

                val objectType = lineFromFile[0]
                val line = lineFromFile.removeRange(0, 2)
                Log.d(TAG, "Reading line: $line")
                when (objectType) {
                    'c' -> {
                        createCarFromLine(line)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { car ->
                                tmpCar = car
                                tmpPart = Part()
                                counterCars++
                                Log.d(TAG, "Reading car: ${tmpCar.getFullName()}")
                            }
                    }
                    'p' -> {
                        createPartFromLine(line, tmpCar.id)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { part ->
                                tmpPart = part
                                counterParts++
                                Log.d(TAG, "Reading part: ${tmpPart.name}")
                            }
                    }
                    'r' -> {
                        createRepairFromLine(line, tmpCar.id, tmpPart.id)
                        counterRepairs++
                        Log.d(TAG, "Reading repair...")
                    }
                    'n' -> {
                        createNoteFromLine(line, tmpCar.id, tmpPart.id)
                        counterNotes++
                        Log.d(TAG, "Reading note...")
                    }
                }
            }
        }
        return Single.just("was read $counterCars car(s), $counterNotes note(s), $counterParts part(s), $counterRepairs repair(s)")
    }

    private fun getDataForFile(repair: Note): String {
        return "\t -> ${repair.description}\n"
    }

    fun getFileWithCarsData(cars: List<Car>): File {
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
    private fun createCarFromLine(string: String): Single<Car> {

        // brand[0], model[1], year[2], win[3], numb[4], odo[5], photo[6] - > type of line from cars.txt

        val sa = string.split(",")

        val car = Car()
        car.brand = sa[0]
        car.model = sa[1]
        car.vin = sa[3]
        car.number = sa[4]
        try {
            car.mileage = sa[5].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading car: ${car.number} - problem with mileage")
        }
        try {
            car.year = sa[2].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading car: ${car.number} - problem with year")
        }
        car.photo = sa[6]

        carService.create(car)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { id -> car.id = id }

        return Single.just(car)
    }

    @SuppressLint("CheckResult")
    private fun createPartFromLine(line: String, carId: Long): Single<Part> {

        // name[0], codes[1], limitKM[2], limitDay[3], dateInstall[4], milleageInstal[5], description[6], photo[7]

        val sa = line.split(",")

        val part = Part()

        part.carId = carId
        part.name = sa[0]
        part.codes = sa[1].replace(':', ',')
        try {
            part.limitKM = sa[2].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading part: ${part.name} - problem with limitkm - ${sa[2]}")
        }
        try {
            part.limitDays = sa[3].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading part: ${part.name} - problem with limitDay - ${sa[3]}")
        }
        try {
            part.dateLastChange = LocalDate.parse(sa[4], dateMapper)
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Reading part: ${part.name} - problem with date last change - ${sa[4]}"
            )
        }
        try {
            part.mileageLastChange = sa[5].toInt()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Reading part: ${part.name} - problem with mileage last change - ${sa[5]}"
            )
        }
        part.description = sa[6]
        part.photo = sa[7]

        partService.create(part)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { id -> part.id = id }

        return Single.just(part)
    }

    @SuppressLint("CheckResult")
    private fun createRepairFromLine(line: String, carId: Long, partId: Long) {

        // type[0], description[1], mileage[2], dateInstall[3], cost[4]
        val sa = line.split(",")

        val repair = Repair()

        repair.carId = carId
        repair.partId = partId
        repair.type = sa[0]
        repair.description = sa[1]
        try {
            repair.cost = sa[4].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading repair: ${repair.type} - problem with cost - ${sa[5]}")
        }
        try {
            repair.mileage = sa[2].toInt()
        } catch (e: Exception) {
            Log.d(TAG, "Reading repair: ${repair.type} - problem with mileage - ${sa[2]}")
        }
        try {
            repair.date = LocalDate.parse(sa[3], dateMapper)
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Reading repair: ${repair.type} - problem with date change - ${sa[3]}"
            )
        }

        repairService.create(repair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    @SuppressLint("CheckResult")
    private fun createNoteFromLine(line: String, carId: Long, partId: Long) {

        // description[0], importantLevel[1], dateInstall[2]
        val sa = line.split(",")

        val note = Note()

        note.carId = carId
        note.partId = partId
        note.description = sa[0]
        try {
            note.importantLevel = NoteLevel.fromInt(sa[1].toInt())!!
        } catch (e: Exception) {
            note.importantLevel = NoteLevel.MIDDLE
            Log.d(TAG, "Reading note: ${note.importantLevel} - problem with date - ${sa[1]}")
        }

        try {
            note.date = LocalDate.parse(sa[2], dateMapper)
        } catch (e: Exception) {
            Log.d(TAG, "Reading note: ${note.date} - problem with date - ${sa[2]}")
        }

        noteService.create(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}