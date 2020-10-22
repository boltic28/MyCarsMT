package com.example.mycarsmt.backServices

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
//import com.example.mycarsmt.domain.service.car.CarServiceImpl.Companion.RESULT_DIAGNOSTIC_FOR_NOTIFICATION

class CarReceiver : BroadcastReceiver() {

    private lateinit var handler: Handler
    private lateinit var carServiceImpl: CarRepositoryImpl
    private lateinit var notification: NotificationCar

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
//        if (context != null) {
//            handler = initHandler(context)
//            notification = NotificationCar(context)
//            notification.createNotificationChannel()
//
//            carServiceImpl = CarServiceImpl(context, handler)
//        }
    }

//    private fun initHandler(context: Context) =
//        Handler(context.mainLooper, Handler.Callback { msg ->
//            if (msg.what == RESULT_DIAGNOSTIC_FOR_NOTIFICATION) {
//                val string = msg.obj as String
//                notification.showNotification(context, string)
//                true
//            } else {
//                false
//            }
//        })

}