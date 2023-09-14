package com.example.chatapp.home.register

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
import com.example.chatapp.home.MainActivity
import com.example.chatapp.home.login.LoginFragment
import com.example.chatapp.util.hideKeyboard
import com.example.chatapp.util.showAlertDialog


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
        onHaveAccountClick()
        observeErrorLiveData()

    }

    private fun observeErrorLiveData() {
        registerViewModel.errorLiveData.observe(viewLifecycleOwner) { viewError ->
            showAlertDialog(
                message = viewError.message ?: "something went wrong",
                posActionName = "ok",
                posAction = { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
        }
    }


    private fun onHaveAccountClick() {
        binding.loginHaveAccountTv.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
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
        setCustomToolbarTitle(getString(R.string.create_account))
        enableBackArrowButton()

    }

    private fun enableBackArrowButton() {
        val activity = requireActivity() as MainActivity
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