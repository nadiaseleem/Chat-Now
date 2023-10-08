package com.example.chatapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.ui.addRoom.AddRoomActivity

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setContentView(binding.root)
        initViews()
        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        viewModel.events.observe(this, ::handleEvents)
    }

    private fun handleEvents(homeViewEvent: HomeViewEvent) {

        when (homeViewEvent) {
            HomeViewEvent.NavigateToAddRoom -> navigateToAddRoom()
        }
    }

    private fun navigateToAddRoom() {
        startActivity(Intent(this, AddRoomActivity::class.java))
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}