package com.example.mycarsmt.presentation.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.mycarsmt.R
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.presentation.fragments.dialogs.CarDeleteDialog

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "testmt"
        const val PERMISSION_REQUEST_CODE = 7777
    }

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MAIN: new start-------------------------------------------")
        checkPermissions()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) loadSettings()
        return super.onOptionsItemSelected(item)
    }

    private fun loadSettings() {
//        Log.d(TAG, "FRAG manager: load settings fragment")
//        fragmentManager.beginTransaction()
//            .replace(
//                R.id.fragmentContainer,
//                SettingFragment.getInstance(),
//                SettingFragment.FRAG_TAG
//            )
//            .addToBackStack(SettingFragment.FRAG_TAG)
//            .commit()
    }

    fun loadMainFragment() {
//        Log.d(TAG, "FRAG manager: create main fragment")
//
//        fragmentManager.beginTransaction()
//            .replace(
//                R.id.fragmentContainer,
//                MainListFragment.getInstance(),
//                MainListFragment.FRAG_TAG
//            )
//            .commit()
//
//        fragmentManager.popBackStack(
//            null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun loadCarFragmentWithoutBackStack(car: Car) {
//        Log.d(TAG, "FRAG manager: create car fragment")
//
//        if (fragmentManager.backStackEntryCount > 1) {
//            fragmentManager.popBackStack(
//                fragmentManager.getBackStackEntryAt(1).id,
//                FragmentManager.POP_BACK_STACK_INCLUSIVE
//            )
//        }
//
//        fragmentManager.beginTransaction()
//            .replace(
//                R.id.fragmentContainer,
//                CarFragment.getInstance(car),
//                CarFragment.FRAG_TAG
//            )
//            .commit()
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