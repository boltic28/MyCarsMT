package com.example.mycarsmt.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.view.CarViewModel
import com.example.mycarsmt.view.adaptors.CarItemAdapter


class MainActivity : AppCompatActivity() {

    private val TAG = "testmt"
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CarItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MAIN: new start-------------------------------------------")

        val mCarViewModel = ViewModelProvider(this).get(CarViewModel::class.java)

        populateBase(mCarViewModel)
        setAdapter()
        setLiveRecycler()

        mCarViewModel.getAll()
            .observe(this, Observer<List<Car>> { cars -> adapter.setCars(cars) })
    }

    private fun setAdapter(){
        adapter = CarItemAdapter(this, object :
            CarItemAdapter.OnCarClickListener {
            override fun onClick(car: Car) {
                Toast.makeText(this@MainActivity, "car ${car.brand} is clicked", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setLiveRecycler(){
        recycler = findViewById(R.id.mainRecycler)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun populateBase(viewModel: CarViewModel){
        val handlerThread = HandlerThread("readThread");
        handlerThread.start();
        val looper = handlerThread.getLooper();
        val handler = Handler(looper);
        handler.post {
            viewModel.getService().testing()
        }
    }
}