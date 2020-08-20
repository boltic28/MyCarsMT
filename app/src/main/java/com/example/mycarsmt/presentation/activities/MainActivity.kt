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
import androidx.navigation.findNavController
import com.example.mycarsmt.R

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "test_mt"
        const val PERMISSION_REQUEST_CODE = 7777
    }

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
        if (item.itemId == R.id.action_settings)
            findNavController(R.id.fragment).navigate(R.id.settingFragment)

        return super.onOptionsItemSelected(item)
    }
}