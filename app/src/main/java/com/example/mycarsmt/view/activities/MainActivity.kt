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
        Log.d(TAG, "FRAG manager: create main fragment")
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                MainListFragment.getInstance(),
                MainListFragment.FRAG_TAG
            )
            .commit()

        if (fragmentManager.backStackEntryCount > 0){
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun loadCarFragment(car: Car) {
        Log.d(TAG, "FRAG manager: create car fragment")

        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CarFragment.getInstance(car),
                CarFragment.FRAG_TAG
            )
            .addToBackStack(CarFragment.FRAG_TAG)
            .commit()
    }

    fun loadCarFragmentWithoutBackStack(car: Car) {
        Log.d(TAG, "FRAG manager: create car fragment")

        if (fragmentManager.backStackEntryCount > 1){
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(1).id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CarFragment.getInstance(car),
                CarFragment.FRAG_TAG
            )
            .commit()
    }

    fun loadPartFragment(part: Part) {
        Log.d(TAG, "FRAG manager: create part fragment")
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                PartFragment.getInstance(part),
                PartFragment.FRAG_TAG
            )
            .addToBackStack(PartFragment.FRAG_TAG)
            .commit()
    }

    fun loadPartFragmentWithoutBackStack(part: Part) {
        Log.d(TAG, "FRAG manager: create part fragment")

        if(fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack(PartFragment.FRAG_TAG, 0)
        }

        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                PartFragment.getInstance(part),
                PartFragment.FRAG_TAG
            )
            .commit()
    }

    fun loadCarCreator(car: Car) {
        Log.d(TAG, "FRAG manager: load carCreator fragment")

        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CarCreator.getInstance(car),
                CarCreator.FRAG_TAG
            )
            .addToBackStack(CarCreator.FRAG_TAG)
            .commit()
    }

    fun loadPartCreator(part: Part) {
        Log.d(TAG, "FRAG manager: load partCreator fragment")
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                PartCreator.getInstance(part),
                PartCreator.FRAG_TAG
            )
            .addToBackStack(PartCreator.FRAG_TAG)
            .commit()
    }

    fun loadNoteCreator(note: Note) {
        Log.d(TAG, "FRAG manager: load noteCreator fragment")
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                NoteCreator.getInstance(note),
                NoteCreator.FRAG_TAG
            )
            .addToBackStack(NoteCreator.FRAG_TAG)
            .commit()
    }

    fun loadRepairCreator(repair: Repair) {
        Log.d(TAG, "FRAG manager: load repairCreator fragment")
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                RepairCreator.getInstance(repair),
                RepairCreator.FRAG_TAG
            )
            .addToBackStack(RepairCreator.FRAG_TAG)
            .commit()
    }

    fun loadDeleter(car: Car) {
        Log.d(TAG, "FRAG manager: load carDeleter fragment")
        fragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                CarDeleteDialog.getInstance(car),
                CarDeleteDialog.FRAG_TAG
            )
            .commit()
    }

    fun loadDeleter(part: Part) {
        Log.d(TAG, "FRAG manager: load partDeleter fragment")
        fragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                PartDeleteDialog.getInstance(part),
                PartDeleteDialog.FRAG_TAG
            )
            .commit()
    }

    fun loadDeleter(repair: Repair) {
        Log.d(TAG, "FRAG manager: load repairDeleter fragment")
        fragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                RepairDeleteDialog.getInstance(repair),
                RepairDeleteDialog.FRAG_TAG
            )
            .commit()
    }

    fun hideFragment(fragment: Fragment) {
        Log.d(TAG, "FRAG manager: load previous frag: $fragment")
        fragmentManager.beginTransaction()
            .hide(fragment)
            .commit()
    }

    fun removeFragment(fragment: Fragment) {
        Log.d(TAG, "FRAG manager: load previous frag: $fragment")
        fragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    // will make test
    fun loadPreviousFragment(fragment: Fragment) {
        Log.d(TAG, "FRAG manager: load previous fragment")
        removeFragment(fragment)
        onBackPressed()
    }

    fun loadPreviousFragment() {
        Log.d(TAG, "FRAG manager: load previous fragment")
        onBackPressed()
    }

    fun loadPreviousFragmentWithStack(tag: String) {
        Log.d(TAG, "FRAG manager: load previous fragment")
        fragmentManager.popBackStack(tag, 0)
    }

    override fun onBackPressed() {
        fragmentManager.findFragmentByTag(CarDeleteDialog.FRAG_TAG)?.let {
            Log.d(TAG, "FRAG manager: hide delete fragment from stack")
            fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentByTag(CarDeleteDialog.FRAG_TAG)!!)
                .commit()
        }
        super.onBackPressed()
    }
}