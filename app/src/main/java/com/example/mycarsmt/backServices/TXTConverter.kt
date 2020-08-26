package com.example.mycarsmt.backServices

import android.util.Log
import com.example.mycarsmt.Directories
import com.example.mycarsmt.data.enums.NoteLevel
import com.example.mycarsmt.domain.*
import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TXTConverter {

    companion object {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)

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

        fun writeCarsToFile(cars: List<Car>): String {
            File(Directories.TXT_OUTPUT_FILE.value).createNewFile()
            var result = ""
            var counter = 0
            cars.forEach {car ->
                result = result.plus(toStringForDB(car))
                counter++
            }
            File(Directories.TXT_OUTPUT_FILE.value).writeText(
                result.substring(
                    0,
                    result.length - 1
                )
            )
            return "$counter cars written to file ${Directories.TXT_OUTPUT_FILE.value}"
        }

        private fun toStringForDB(car: Car): String {

            var line = "c ${car.brand},${car.model},${car.year},${car.vin},${car.number}," +
                    "${car.mileage},${car.photo}\n"

            car.notes.stream()
                .filter { it.partId != 0L }
                .forEach { line = line.plus(toStringForDB(it)) }

            car.repairs.stream()
                .filter { it.partId != 0L }
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


        fun readCarFromFile(): String {

            val f = File(Directories.TXT_FILE_WITH_CARS.value)
            val inputStream: InputStream = f.inputStream()
            var counterCars = 0
            var counterParts = 0
            var counterNotes = 0
            var counterRepairs = 0
            lateinit var tmpCar: Car
            lateinit var tmpPart: Part

            inputStream.bufferedReader().useLines { lines ->
                lines.forEach {lineFromFile ->
                    Log.d("READ", "Reading file: $lineFromFile")

                    val objectType = lineFromFile[0]
                    val line = lineFromFile.removeRange(0, 2)
                    Log.d("READ", "Reading line: $line")
                    when (objectType) {
                        'c' -> {
                            tmpCar = createCarFromLine(line)
                            tmpPart = Part()
                            counterCars++
                        }
                        'p' -> {
                            tmpPart = createPartFromLine(line, tmpCar.id)
                            counterParts++
                        }
                        'r' -> {
                            createRepairFromLine(line, tmpCar.id, tmpPart.id)
                            counterRepairs++
                        }
                        'n' -> {
                            createNoteFromLine(line, tmpCar.id, tmpPart.id)
                            counterNotes++
                        }
                    }
                }
            }
            return "was read $counterCars car(s), $counterNotes note(s), $counterParts part(s), $counterRepairs repair(s)"
        }

        fun writeFileWithNotes(cars: List<Car>): File {
            testCommonDirectory()
            val file = File(Directories.TO_NOTES_OUTPUT_FILE.value)
            file.createNewFile()
            var result = ""
            cars.forEach {car ->
                val notes = car.notes
                if (notes.isNotEmpty()) {
                    result = result.plus("${car.getFullName()} + \n")
                    notes.forEach {note ->
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

        private fun getDataForFile(repair: Note): String{
            return "\t -> ${repair.description}\n"
        }

        fun getFileWithCarsData(cars: List<Car>): File {
            testCommonDirectory()
            val file = File(Directories.DATA_OUTPUT_FILE.value)
            file.createNewFile()
            var result = ""
            cars.forEach {car ->
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

        private fun testCommonDirectory() {
            val file = File(Directories.COMMON_DATA_DIRECTORY.value)
            if (!file.exists())
                file.mkdirs()
        }

        private fun createCarFromLine(string: String): Car {

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
                Log.d("READ", "Reading car: ${car.number} - problem with mileage")
            }
            try {
                car.year = sa[2].toInt()
            } catch (e: Exception) {
                Log.d("READ", "Reading car: ${car.number} - problem with year")
            }
            car.photo = sa[6]
            //car.create() todo creating the car
            return car
        }

        private fun createPartFromLine(line: String, carId: Long): Part {

            // name[0], codes[1], limitKM[2], limitDay[3], dateInstall[4], milleageInstal[5], description[6], photo[7]

            val sa = line.split(",")

            val part = Part()

            part.carId = carId
            part.name = sa[0]
            part.codes = sa[1].replace(':', ',')
            try {
                part.limitKM = sa[2].toInt()
            } catch (e: Exception) {
                Log.d("READ", "Reading part: ${part.name} - problem with limitkm - ${sa[2]}")
            }
            try {
                part.limitDays = sa[3].toInt()
            } catch (e: Exception) {
                Log.d("READ", "Reading part: ${part.name} - problem with limitDay - ${sa[3]}")
            }
            try {
                part.dateLastChange = LocalDate.parse(sa[4], formatter)
            } catch (e: Exception) {
                Log.d(
                    "READ",
                    "Reading part: ${part.name} - problem with date last change - ${sa[4]}"
                )
            }
            try {
                part.mileageLastChange = sa[5].toInt()
            } catch (e: Exception) {
                Log.d(
                    "READ",
                    "Reading part: ${part.name} - problem with mileage last change - ${sa[5]}"
                )
            }
            part.description = sa[6]
            part.photo = sa[7]

            //part.create() todo creating the part
            return part
        }

        private fun createRepairFromLine(line: String, carId: Long, partId: Long): Repair {

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
                Log.d("READ", "Reading repair: ${repair.type} - problem with cost - ${sa[5]}")
            }
            try {
                repair.mileage = sa[2].toInt()
            } catch (e: Exception) {
                Log.d("READ", "Reading repair: ${repair.type} - problem with mileage - ${sa[2]}")
            }
            try {
                repair.date = LocalDate.parse(sa[3], formatter)
            } catch (e: Exception) {
                Log.d(
                    "READ",
                    "Reading repair: ${repair.type} - problem with date change - ${sa[3]}"
                )
            }

            //repair.create() todo creating repair
            return repair
        }

        private fun createNoteFromLine(line: String, carId: Long, partId: Long): Note {

            // description[0], importantLevel[1], dateInstall[2]
            val sa = line.split(",")

            val note = Note()

            note.carId = carId
            note.description = sa[0]
            try {
                note.importantLevel = NoteLevel.fromInt(sa[1].toInt())!!
            } catch (e: Exception) {
                note.importantLevel = NoteLevel.MIDDLE
                Log.d("READ", "Reading note: ${note.importantLevel} - problem with date - ${sa[1]}")
            }

            try {
                note.date = LocalDate.parse(sa[2], formatter)
            } catch (e: Exception) {
                Log.d("READ", "Reading note: ${note.date} - problem with date - ${sa[2]}")
            }

//            note.create() todo creating note
            return note
        }
    }
}