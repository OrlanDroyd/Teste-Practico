package com.gmail.orlandroyd.testepratico.presentation.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gmail.orlandroyd.testepratico.R
import com.gmail.orlandroyd.testepratico.databinding.FragmentLoginBinding
import com.gmail.orlandroyd.testepratico.util.DataState
import com.gmail.orlandroyd.testepratico.util.setInvisible
import com.gmail.orlandroyd.testepratico.util.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    // BINDING
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // VIEW MODEL
    private val viewModel by viewModels<LoginViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init binding
        _binding = FragmentLoginBinding.bind(view)

        // Forgot password msg
        binding.tvForgotMyPassword.setOnClickListener {
            Toast.makeText(context, "Username: 'admin' \nPassword: 'admin'", Toast.LENGTH_SHORT)
                .show()
        }

        // Validation
        viewModel.loginFormState.observe(viewLifecycleOwner,

            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }

                binding.btnLogin.isEnabled = loginFormState.isDataValid

                loginFormState.usernameError?.let { msg ->
                    binding.inputUsername.error = getString(msg)
                }

                loginFormState.passwordError?.let { msg ->
                    binding.inputPassword.error = getString(msg)
                }

            })

        // Add TextWatcher to EditTexts
        binding.inputUsername.doAfterTextChanged {
            viewModel.loginDataChanged(
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString()
            )
        }
        binding.inputPassword.doAfterTextChanged {
            viewModel.loginDataChanged(
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString()
            )
        }

        // Login onClick
        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isEnabled = false
            displayProgressBar(true)
            setupObservers()
        }
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector", "ShowToast")
    private fun setupObservers() {

        val username: String = binding.inputUsername.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()

        // login event
        viewModel.doLogin(username, password)

        // using coroutines to collect result
        lifecycleScope.launch {

            viewModel.login.collect {

                when (it.status) {

                    DataState.Status.SUCCESS -> {
                        if (it.data?.status == "fail") {
                            displayProgressBar(false)
                            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT)
                                .show()
                        } else if (it.data?.status == "success") {
                            displayProgressBar(false)
                            findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavMyProducts())
                        }
                    }

                    DataState.Status.ERROR -> {
                        displayProgressBar(false)
                        Toast.makeText(
                            context,
                            it.message ?: "Invalid credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    DataState.Status.LOADING -> {
                        displayProgressBar(true)
                    }

                    DataState.Status.FAILURE -> {
                        displayProgressBar(false)
                        Toast.makeText(context, it.message ?: "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // LOADING
    private fun displayProgressBar(isDisplayed: Boolean) {
        if (isDisplayed) {
            binding.progressBar.setVisible()
            binding.btnLogin.isEnabled = false
            binding.tvForgotMyPassword.isEnabled = false
            binding.inputUsername.isEnabled = false
            binding.inputPassword.isEnabled = false
        } else {
            binding.progressBar.setInvisible()
            binding.btnLogin.isEnabled = true
            binding.tvForgotMyPassword.isEnabled = true
            binding.inputUsername.isEnabled = true
            binding.inputPassword.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}