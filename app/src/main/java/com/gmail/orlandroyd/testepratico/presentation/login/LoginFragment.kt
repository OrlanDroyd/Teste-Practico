package com.gmail.orlandroyd.testepratico.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.gmail.orlandroyd.testepratico.util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val RC_SIGN_IN = 123

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

        // Google signIn
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(binding.root.context, gso)
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(binding.root.context)
        updateUI(account)
        // Google onClick
        binding.loginGoogle.setOnClickListener {
            signInGoogle(mGoogleSignInClient)
        }

        // Facebook signIn
        binding.loginFacebook.setOnClickListener {
            signInFacebook()
        }
    }

    // TODO: Facebook signIn
    private fun signInFacebook() {
        context.toast("Not implemented yet")
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null)
            findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavMyProducts())
    }

    private fun signInGoogle(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
            context.toast(account?.displayName.toString())
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

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
                            context.toast(getString(R.string.invalid_credentials))
                        } else if (it.data?.status == "success") {
                            displayProgressBar(false)
                            findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavMyProducts())
                        }
                    }

                    DataState.Status.ERROR -> {
                        displayProgressBar(false)
                        context.toast(it.message ?: getString(R.string.invalid_credentials))
                    }

                    DataState.Status.LOADING -> {
                        displayProgressBar(true)
                    }

                    DataState.Status.FAILURE -> {
                        displayProgressBar(false)
                        context.toast(it.message ?: getString(R.string.error))
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