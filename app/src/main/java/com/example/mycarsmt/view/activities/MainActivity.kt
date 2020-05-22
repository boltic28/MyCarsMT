package com.example.mycarsmt.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.view.adaptors.CarItemAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "testmt"
    private lateinit var adapter: CarItemAdapter

    private lateinit var service: CarServiceImpl
    private lateinit var cars: List<Car>
    private lateinit var handler: Handler

    companion object {
        const val RESULT_ALL_READED = 101
        const val RESULT_CAR_READED = 102
        const val RESULT_CAR_CREATED = 103
        const val RESULT_CAR_UPDATED = 104
        const val RESULT_CAR_DELETED = 105
        const val RESULT_CAR_NOTES_COUNT = 106
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MAIN: new start-------------------------------------------")

        setHandler()
        cars = emptyList()
        service = CarServiceImpl(this, handler)
        service.readAll()

//        populateBase(service)

        setRecycler()
        setAdapter()
    }

    private fun setHandler() {
        handler = Handler(mainLooper, Handler.Callback { msg ->
            Log.d(TAG, "Handler: took contacts from database: result " + msg.what)
            when (msg.what) {

                RESULT_ALL_READED -> {
                    cars = msg.obj as List<Car>
                    Log.d(
                        TAG,
                        "Handler: took contacts from database: list size ${cars.size}"
                    )
//                    setAdapter()
                    carsIsEmpty()
                    adapter.setCars(cars)
                    true
                }

//                RESULT_CAR_READED -> {
//
//                }
//                RESULT_CAR_CREATED -> {
//                }
//                RESULT_CAR_UPDATED -> {
//                }
//                RESULT_CAR_DELETED -> {
//                }

                else -> {
                    false
                }
            }
        })
    }

    private fun setRecycler() {
        mainRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setAdapter()
    }

    private fun setAdapter() {
        mainRecycler.adapter = CarItemAdapter(cars, object :
            CarItemAdapter.OnCarClickListener {
            override fun onClick(car: Car) {
                Toast.makeText(
                    this@MainActivity,
                    "car ${car.brand} is clicked",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun carsIsEmpty() {
        if (cars.isEmpty()) {
            emptyList.visibility = View.VISIBLE
        } else {
            emptyList.visibility = View.GONE
        }
    }

    private fun populateBase(service: CarServiceImpl) {
        val handlerThread = HandlerThread("readThread");
        handlerThread.start();
        val looper = handlerThread.getLooper();
        val handler = Handler(looper);
        handler.post {
            service.testing()
        }
    }
}