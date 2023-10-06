package com.example.chatapp.ui.authentication.login

import android.content.Intent
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
import com.example.chatapp.ui.authentication.AuthenticationActivity
import com.example.chatapp.ui.authentication.register.RegisterFragment
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.util.hideKeyboard
import com.example.chatapp.util.showMessage

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
        subscribeToLiveDate()

    }

    private fun handleEvents(loginViewEvents: LoginViewEvents) {

        when (loginViewEvents) {
            LoginViewEvents.NavigateToHome -> navigateToHome()
            LoginViewEvents.NavigateToRegister -> navigateToRegister()
        }
    }

    private fun subscribeToLiveDate() {
        loginViewModel.messageLiveData.observe(viewLifecycleOwner) { message ->
            showMessage(
                message = message.message ?: "something went wrong",
                posActionName = "ok",
                posAction = message.posAction,
                negActionName = message.negName,
                negAction = message.negAction
            )
        }

        loginViewModel.events.observe(viewLifecycleOwner, ::handleEvents)

        loginViewModel.hideKeyboard.observe(viewLifecycleOwner) { hide ->
            if (hide)
                hideKeyboard()
        }
    }


    private fun navigateToRegister() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RegisterFragment())
            .addToBackStack(null)
            .commit()

    }

    private fun navigateToHome() {
        startActivity(Intent(activity, HomeActivity::class.java))
        requireActivity().finish()
    }

    fun hideKeyboard() {
        view?.hideKeyboard(activity as AppCompatActivity?)
    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(getString(R.string.login))
        disableBackArrowButton()

    }


    private fun disableBackArrowButton() {
        val activity = requireActivity() as AuthenticationActivity
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