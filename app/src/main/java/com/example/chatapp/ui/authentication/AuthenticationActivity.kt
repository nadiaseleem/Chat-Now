package com.example.chatapp.ui.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityAuthenticationBinding
import com.example.chatapp.ui.authentication.login.LoginFragment

class AuthenticationActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LoginFragment())
            .commit()

        binding.lifecycleOwner = this

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}