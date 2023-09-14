package com.example.chatapp.home.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.home.MainActivity
import com.example.chatapp.home.register.RegisterFragment
import com.example.chatapp.util.hideKeyboard
import com.example.chatapp.util.showAlertDialog

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.vm = loginViewModel
        binding.lifecycleOwner = this
        hideKeyboard()
        onCreateAccountClick()
        observeErrorLiveData()

    }

    private fun observeErrorLiveData() {
        loginViewModel.errorLiveData.observe(viewLifecycleOwner) { viewError ->
            showAlertDialog(
                message = viewError.message ?: "something went wrong",
                posActionName = "ok",
                posAction = { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
        }
    }

    private fun onCreateAccountClick() {
        binding.tvDontHaveAccount.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun hideKeyboard() {
        binding.constraint.setOnClickListener {
            it.hideKeyboard(activity as AppCompatActivity?)
        }
    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(getString(R.string.login))
        disableBackArrowButton()

    }


    private fun disableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowHomeEnabled(false)
    }


    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

            val params = toolbarTitle.layoutParams as ViewGroup.MarginLayoutParams
            params.marginEnd = 0
            toolbarTitle.layoutParams = params

        }
    }


}