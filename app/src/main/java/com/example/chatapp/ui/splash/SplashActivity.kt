package com.example.chatapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.AuthenticationActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivity()
    }

    private fun startActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }, 2000)
    }
}