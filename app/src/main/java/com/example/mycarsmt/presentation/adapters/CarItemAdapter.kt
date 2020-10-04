package com.example.mycarsmt.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.NO_PHOTO
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.domain.Car
import com.squareup.picasso.Picasso
import java.io.File

class CarItemAdapter(
    carsIn: List<Car>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<CarItemAdapter.CarHolder>() {

    private var cars = carsIn

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false))

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: CarHolder, position: Int) = holder.bind(cars[position])

    inner class CarHolder(private val carRow: View) : RecyclerView.ViewHolder(carRow) {

        private val name = carRow.findViewById<TextView>(R.id.carItemCarName)
        private val number = carRow.findViewById<TextView>(R.id.carItemNumber)
        private val mileage = carRow.findViewById<TextView>(R.id.carItemMileage)
        private val photo = carRow.findViewById<ImageView>(R.id.carItemImage)
        private val refreshIcon = carRow.findViewById<ImageView>(R.id.carItemIconRefresh)
        private val attentionIcon = carRow.findViewById<ImageView>(R.id.carItemIconAttention)
        private val serviceIcon = carRow.findViewById<ImageView>(R.id.carItemIconService)
        private val buyIcon = carRow.findViewById<ImageView>(R.id.carItemIconBuy)
        private val infoIcon = carRow.findViewById<ImageView>(R.id.carItemIconInfo)

        fun bind(car: Car) {
            var line = car.brand + " " + car.model
            if (line.length > 15) {
                line = line.substring(0, 13) + ".."
            }
            name.text = line
            number.text = car.number
            if (car.condition.contains(Condition.CHECK_MILEAGE)) {
                mileage.text = carRow.resources.getString(R.string.car_item_update_mileage, car.mileage)
                mileage.setTextColor(Color.argb(255, 230, 5, 5))
            } else {
                mileage.text = carRow.resources.getString(R.string.car_item_normal_mileage, car.mileage)
            }

            attentionIcon.setColorFilter(
                if (car.condition.contains(Condition.ATTENTION))
                    Color.argb(255, 230, 5, 5)
                else
                    Color.argb(255, 208, 208, 208)
            )
            serviceIcon.setColorFilter(
                if (car.condition.contains(Condition.MAKE_SERVICE))
                    Color.argb(255, 230, 120, 5)
                else
                    Color.argb(255, 208, 208, 208)
            )

            buyIcon.setColorFilter(
                if (car.condition.contains(Condition.BUY_PARTS))
                    Color.argb(255, 180, 180, 5)
                else
                    Color.argb(255, 208, 208, 208)
            )
            infoIcon.setColorFilter(
                if (car.condition.contains(Condition.MAKE_INSPECTION))
                    Color.argb(255, 10, 120, 5)
                else
                    Color.argb(255, 208, 208, 208)
            )
            refreshIcon.setColorFilter(
                if (car.condition.contains(Condition.CHECK_MILEAGE))
                    Color.argb(255, 90, 30, 5)
                else
                    Color.argb(255, 208, 208, 208)
            )

            if (car.photo == NO_PHOTO || car.photo.isEmpty()) {
                Picasso.get().load(R.drawable.nophoto).into(photo)
            } else {
                Picasso.get()
                    .load(File(Directories.CAR_IMAGE_DIRECTORY.value,
                        carRow.resources.getString(R.string.photo_path, car.photo)))
                    .into(photo)
            }

            mileage.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMileageClick(cars[adapterPosition])
                }
            }

            carRow.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(cars[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(car: Car)
        fun onMileageClick(car: Car)
    }
}