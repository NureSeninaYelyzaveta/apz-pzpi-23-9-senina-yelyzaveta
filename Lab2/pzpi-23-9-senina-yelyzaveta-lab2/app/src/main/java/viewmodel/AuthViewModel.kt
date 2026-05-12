package com.example.artprogressmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artprogressmobile.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // Стан екрану авторизації
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state

    // Логін студента
    fun loginStudent(email: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            _state.value = AppState(loading = true)

            try {
                val student = RetrofitClient.apiService.loginStudent(email)
                onSuccess(student.studentID)

                _state.value = AppState()

            } catch (e: Exception) {
                _state.value = AppState(error = "Помилка входу студента")
            }
        }
    }

    // Логін батьків
    fun loginParent(email: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            _state.value = AppState(loading = true)

            try {
                val parent = RetrofitClient.apiService.getParentByEmail(email)
                onSuccess(parent.parentID)

                _state.value = AppState()

            } catch (e: Exception) {
                _state.value = AppState(error = "Помилка входу батьків")
            }
        }
    }
}