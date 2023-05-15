package com.project.seorigami.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.project.seorigami.R
import com.project.seorigami.util.Prefs

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        moveActivity()
    }

    private fun checkPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).withListener(object : com.karumi.dexter.listener.multi.MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let {
                        if(p0.areAllPermissionsGranted()){
                            moveActivity()
                        } else {
//                                intentToMain()
                            //create toast
                            Toast.makeText(this@SplashScreenActivity, "Tolong berikan semua izin untuk aplikasi", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            })
            .check()
    }

    private fun moveActivity() {
        Handler().postDelayed({
            if (Prefs(this).jwt.isNullOrEmpty()) {
                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        },3000)
    }
}