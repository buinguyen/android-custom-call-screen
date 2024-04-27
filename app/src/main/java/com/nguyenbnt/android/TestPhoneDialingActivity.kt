package com.nguyenbnt.android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nguyenbnt.android.databinding.ActivityTestPhoneDialingBinding

class TestPhoneDialingActivity : Activity() {

    private lateinit var binding: ActivityTestPhoneDialingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestPhoneDialingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestAppPermissions()
    }

    private fun requestAppPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG
        )
        if (!isAllGranted(applicationContext, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PHONE_PERMISSIONS_RC)
        } else if (!isDrawOverlaysGranted(applicationContext)) {
            requestDrawOverlaysPermission()
        }
    }

    private fun requestDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.fromParts("package", packageName, null),
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PHONE_PERMISSIONS_RC) {
            val isAllGranted = isAllGranted(grantResults)
            if (!isAllGranted) {
                Toast.makeText(this, "Requires permission", Toast.LENGTH_LONG).show()
            } else if (!isDrawOverlaysGranted(applicationContext)) {
                requestDrawOverlaysPermission()
            }
        }
    }

    companion object {
        private const val PHONE_PERMISSIONS_RC = 101

        fun isAllGranted(context: Context, permissions: Array<String>): Boolean {
            for (permission in permissions) {
                val result = ContextCompat.checkSelfPermission(context, permission)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        fun isAllGranted(grantResults: IntArray): Boolean {
            val failResults = grantResults.filter { it != PackageManager.PERMISSION_GRANTED }
            return failResults.isEmpty()
        }

        fun isDrawOverlaysGranted(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context)
            } else {
                true
            }
        }
    }
}
