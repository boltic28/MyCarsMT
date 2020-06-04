package com.example.mycarsmt.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.view.creators.CarCreator
import com.example.mycarsmt.view.creators.NoteCreator
import com.example.mycarsmt.view.creators.PartCreator
import com.example.mycarsmt.view.creators.RepairCreator
import com.example.mycarsmt.view.fragments.*

class MainActivity : AppCompatActivity() {

    private val TAG = "testmt"
    private val PERMISSION_REQUEST_CODE = 7777

    private lateinit var fragmentManager: FragmentManager

    fun getMainFragmentManager() = fragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MAIN: new start-------------------------------------------")
        checkPermissions()

        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, MainListFragment.getInstance(), MainListFragment.FRAG_TAG)
            .commit()
    }

    private fun checkPermissions() {
        val checkPermissionResultStorageWrite = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val checkPermissionResultStorageRead = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val checkPermissionResultCamera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )

        if (checkPermissionResultStorageWrite != PackageManager.PERMISSION_GRANTED &&
            checkPermissionResultCamera != PackageManager.PERMISSION_GRANTED &&
            checkPermissionResultStorageRead != PackageManager.PERMISSION_GRANTED
        ) {
            val permissionArray =
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            ActivityCompat.requestPermissions(this, permissionArray, PERMISSION_REQUEST_CODE)
        }
    }

    fun loadMainFragment() {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                MainListFragment.getInstance(),
                MainListFragment.FRAG_TAG
            )
            .commit()
    }

    fun loadCarFragment(car: Car) {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CarFragment.getInstance(car),
                CarFragment.FRAG_TAG
            )
            .addToBackStack(PartFragment.FRAG_TAG)
            .commit()
    }

    fun loadPartFragment(part: Part) {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                PartFragment.getInstance(part),
                PartFragment.FRAG_TAG
            )
            .addToBackStack(PartFragment.FRAG_TAG)
            .commit()
    }

    fun loadCarCreator(car: Car) {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CarCreator.getInstance(car),
                CarCreator.FRAG_TAG
            )
            .commit()
    }

    fun loadPartCreator(part: Part) {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                PartCreator.getInstance(part),
                PartCreator.FRAG_TAG
            )
            .commit()
    }

    fun loadNoteCreator(note: Note) {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                NoteCreator.getInstance(note),
                NoteCreator.FRAG_TAG
            )
            .commit()
    }

    fun loadRepairCreator(repair: Repair) {
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                RepairCreator.getInstance(repair),
                RepairCreator.FRAG_TAG
            )
            .commit()
    }

    fun loadDeleter(car: Car) {
        fragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                CarDeleteDialog.getInstance(car),
                CarDeleteDialog.FRAG_TAG
            )
            .commit()
    }

    fun loadDeleter(part: Part) {
        fragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                PartDeleteDialog.getInstance(part),
                PartDeleteDialog.FRAG_TAG
            )
            .commit()
    }

    fun loadDeleter(repair: Repair) {
        fragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                RepairDeleteDialog.getInstance(repair),
                RepairDeleteDialog.FRAG_TAG
            )
            .commit()
    }

    fun hideFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .hide(fragment)
            .commit()
    }
// will make test
    fun loadPreviousFragment() {
        fragmentManager.popBackStack()
    }
}