package com.example.chatapp.ui.addRoom

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityAddRoomBinding
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.util.hideKeyboard
import com.example.chatapp.util.showMessage

class AddRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRoomBinding
    private val viewModel: AddRoomViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_room)
        setContentView(binding.root)
        setCategoriesAdapter()

        initViews()

        subscribeToLiveData()
    }


    private fun setCategoriesAdapter() {
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapterLanguages =
            ArrayAdapter(this, R.layout.drop_down_item, categories)
        binding.content.autoCompleteTVCategories.setAdapter(arrayAdapterLanguages)

    }

    private fun subscribeToLiveData() {
        viewModel.hideKeyboard.observe(this) { hide ->
            if (hide)
                hideKeyboard()
        }
        viewModel.messageLiveData.observe(this) { message ->
            showMessage(
                message = message.message ?: "",
                posActionName = "ok",
                message.posAction,
                message.negName,
                message.negAction
            )
        }
        viewModel.events.observe(this, ::handleEvents)
    }

    private fun handleEvents(addRoomViewEvent: AddRoomViewEvent) {

        when (addRoomViewEvent) {
            AddRoomViewEvent.NavigateToHome -> navigateToHome()
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.content.autoCompleteTVCategories.setSelection(0)
        binding.content.autoCompleteTVCategories.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemView: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onCategorySelected(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}