package com.example.chatapp.ui.authentication.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentRegisterBinding
import com.example.chatapp.ui.authentication.AuthenticationActivity
import com.example.chatapp.ui.authentication.login.LoginFragment
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.util.hideKeyboard
import com.example.chatapp.util.showMessage


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.vm = registerViewModel
        binding.lifecycleOwner = this
        hideKeyboard()
        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {

        registerViewModel.messageLiveData.observe(viewLifecycleOwner) { message ->
            showMessage(
                message = message.message ?: "something went wrong",
                posActionName = "ok",
                posAction = message.posAction,
                negActionName = message.negName,
                negAction = message.negAction,
                isCancelable = message.isCancelable
            )
        }

        registerViewModel.events.observe(viewLifecycleOwner, ::handleEvents)
        registerViewModel.hideKeyboard.observe(viewLifecycleOwner) { hide ->
            if (hide)
                hideKeyboard()
        }

    }

    private fun handleEvents(registerViewEvent: RegisterViewEvent) {
        when (registerViewEvent) {
            RegisterViewEvent.NavigateToHome -> navigateToHome()
            RegisterViewEvent.NavigateToLogin -> navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }

    private fun navigateToHome() {
        startActivity(Intent(activity, HomeActivity::class.java))
        requireActivity().finish()
    }


    private fun hideKeyboard() {
        view?.hideKeyboard(activity as AppCompatActivity?)
    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(getString(R.string.create_account))
        enableBackArrowButton()

    }

    private fun enableBackArrowButton() {
        val activity = requireActivity() as AuthenticationActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

            val actionBarHeight = resources.getDimensionPixelSize(R.dimen.action_bar_default_height)
            val params = toolbarTitle.layoutParams as ViewGroup.MarginLayoutParams
            params.marginEnd = actionBarHeight
            toolbarTitle.layoutParams = params


        }
    }


}