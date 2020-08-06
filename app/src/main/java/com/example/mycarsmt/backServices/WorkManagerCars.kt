package com.example.mycarsmt.backServices

import android.content.Context
import android.os.Handler
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mycarsmt.domain.service.car.CarServiceImpl

class WorkManagerCars(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

//    private var handler: Handler
//    private var carService:CarServiceImpl
//    private var notification: NotificationCar

    init {
//        handler = initHandler(context)
//        carService = CarServiceImpl(context, handler)
//        notification = NotificationCar(context)
//        notification.createNotificationChannel()
    }

    override fun doWork(): Result {
//        carService.makeDiagnosticForNotification()
        return Result.success()
    }

//    private fun initHandler(context: Context) =
//        Handler(context.mainLooper, Handler.Callback { msg ->
//            if (msg.what == CarServiceImpl.RESULT_DIAGNOSTIC_FOR_NOTIFICATION) {
//                val string = msg.obj as String
//                notification.showNotification(context, string)
//                true
//            } else {
//                false
//            }
//        })
}