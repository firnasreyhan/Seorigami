package com.project.seorigami.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.project.seorigami.R
import com.project.seorigami.util.Prefs

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        moveActivity()
    }

    private fun moveActivity() {
        Handler().postDelayed({
            if (Prefs(this).jwt.isNullOrEmpty()) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
//            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        },3000)
    }
}