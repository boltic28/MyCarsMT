package com.example.mycarsmt.view.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.enums.CarCondition
import com.example.mycarsmt.model.repo.car.CarServiceImpl

class CarItemAdapter(
    context: Context, private val listener: OnCarClickListener
) : RecyclerView.Adapter<CarItemAdapter.CarHolder>() {

    private lateinit var cars: List<Car>
    private val service = CarServiceImpl(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent))

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: CarHolder, position: Int) = holder.bind(cars[position])

    fun setCars(list: List<Car>) {
        cars = list
        notifyDataSetChanged()
    }

    inner class CarHolder(private val carRow: View) : RecyclerView.ViewHolder(carRow) {

        private val name = carRow.findViewById<TextView>(R.id.carItemCarName)
        private val number = carRow.findViewById<TextView>(R.id.carItemNumber)
        private val mileage = carRow.findViewById<TextView>(R.id.carItemMileage)
        private val notes = carRow.findViewById<TextView>(R.id.carItemIconInfoMsgCount)
        private val photo = carRow.findViewById<ImageView>(R.id.carItemImage)
        private val attentionIcon = carRow.findViewById<ImageView>(R.id.carItemIconAttention)
        private val serviceIcon = carRow.findViewById<ImageView>(R.id.carItemIconService)
        private val buyIcon = carRow.findViewById<ImageView>(R.id.carItemIconBuy)
        private val infoIcon = carRow.findViewById<ImageView>(R.id.carItemIconInfo)

        @SuppressLint("SetTextI18n")
        fun bind(car: Car) {
            name.text = car.brand + " " + car.model
            number.text = car.number
            mileage.text = car.mileage.toString()

            if (car.condition.contains(CarCondition.ATTENTION))
                attentionIcon.setColorFilter(R.color.colorAttention)
            if (car.condition.contains(CarCondition.MAKE_SERVICE))
                serviceIcon.setColorFilter(R.color.colorService)
            if (car.condition.contains(CarCondition.BUY_PARTS))
                buyIcon.setColorFilter(R.color.colorBuy)
            if (car.condition.contains(CarCondition.MAKE_INSPECTION))
                infoIcon.setColorFilter(R.color.colorInfo)

            val notesCount = service.getCountOfNotes(car)!!
            if (notesCount > 0) notes.visibility = View.VISIBLE
            notes.text = service.getCountOfNotes(car).toString()

            carRow.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(cars[adapterPosition])
                }
            }
        }


    }

    interface OnCarClickListener {
        fun onClick(car: Car)
    }
}