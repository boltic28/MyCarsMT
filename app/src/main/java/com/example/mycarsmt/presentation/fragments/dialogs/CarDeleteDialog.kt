package com.example.mycarsmt.presentation.fragments.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.backServices.TXTConverter
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.service.car.CarRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class CarDeleteDialog @Inject constructor()  : DialogFragment() {

    companion object {
        const val TAG = "test_mt"
        const val FRAG_TAG = "deleter"
    }

    @Inject
    lateinit var carService: CarRepositoryImpl
    lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.injectDialog(this)
        car = arguments?.getSerializable(CAR) as Car
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_delete, container, false)

        view.findViewById<TextView>(R.id.deleteFragmentQuestion).text =
            resources.getString(R.string.dialog_car_delete_really, car.brand, car.model)
        view.findViewById<Button>(R.id.deleteFragmentButtonCancel).setOnClickListener {
            view.findNavController().navigateUp()
        }
        view.findViewById<Button>(R.id.deleteFragmentButtonDelete).setOnClickListener {
            carService.delete(car)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { isDeleted ->
                    if (car.photo != SpecialWords.NO_PHOTO)
                        File(File(Directories.CAR_IMAGE_DIRECTORY.value),
                            (resources.getString(R.string.photo_path, car.photo))).delete()
                    TXTConverter().createCarCopyToFile(car)
                    Log.d(TAG, "DELETE: $isDeleted car(s) was deleted")
                    view.findNavController().navigate(R.id.action_carDeleteDialog_to_mainListFragment)
                },
                { err ->
                    Log.d(TAG, "ERROR: car deleting is fail: $err")
                    view.findNavController().navigateUp()
                }
            )
        }
        return view
    }
}