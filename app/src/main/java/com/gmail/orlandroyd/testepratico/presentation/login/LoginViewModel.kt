package com.gmail.orlandroyd.testepratico.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.orlandroyd.testepratico.R
import com.gmail.orlandroyd.testepratico.domain.model.User
import com.gmail.orlandroyd.testepratico.domain.repository.LoginRepository
import com.gmail.orlandroyd.testepratico.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    /**
     * Validation Form
     */
    private val _username = MutableLiveData("")
    private val _password = MutableLiveData("")
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    // Validation logic
    fun loginDataChanged(username: String, password: String) {
        if (!isUsernameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            _username.value = username
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            _password.value = password
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUsernameValid(username: String) = username.isNotBlank()

    // A placeholder password validation check
    private fun isPasswordValid(password: String) = password.isNotBlank()

    /**
     * Login
     */
    private val _login = Channel<DataState<User>>(Channel.BUFFERED)
    val login = _login.receiveAsFlow()

    /**
     * Simulate login request
     * if username = "admin"
     * and password = "admin"
     * login success
     */
    fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                loginRepository.login(password, password)
                    .catch { e ->
                        delay(2000)
                        _login.send(DataState.error(e.toString()))
                    }
                    .collect {
                        delay(2000)
                        _login.send(it)
                    }
            }
        }
    }
}