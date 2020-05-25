package com.example.mycarsmt.view.adaptors

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.enums.Condition

class CarItemAdapter(
    carsIn: List<Car>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<CarItemAdapter.CarHolder>() {

    private var cars = carsIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false))

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
            var line = "${car.brand} ${car.model}"
            if (line.length > 15) {
                line = line.substring(13) + ".."
            }
            name.text = line
            number.text = car.number
            mileage.text = "${car.mileage} km"

            if (car.condition.contains(Condition.ATTENTION))
                attentionIcon.setColorFilter(Color.argb(255, 230, 5, 5))
            if (car.condition.contains(Condition.MAKE_SERVICE))
                serviceIcon.setColorFilter(Color.argb(255, 230, 120, 5))
            if (car.condition.contains(Condition.BUY_PARTS))
                buyIcon.setColorFilter(Color.argb(255, 180, 180, 5))
            if (car.condition.contains(Condition.MAKE_INSPECTION))
                infoIcon.setColorFilter(Color.argb(255, 10, 120, 5))

//            val notesCount = service.getCountOfNotes(car)!!
//            if (notesCount > 0) notes.visibility = View.VISIBLE
//            notes.text = service.getCountOfNotes(car).toString()

            carRow.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(cars[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(car: Car)
    }
}