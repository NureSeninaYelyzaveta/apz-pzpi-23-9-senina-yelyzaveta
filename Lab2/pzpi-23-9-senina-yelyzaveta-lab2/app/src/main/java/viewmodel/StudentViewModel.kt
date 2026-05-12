package com.example.artprogressmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artprogressmobile.network.RetrofitClient
import com.example.artprogressmobile.network.dto.StudentDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StudentState(
    val student: StudentDto? = null,
    val loading: Boolean = false,
    val error: String = ""
)

class StudentViewModel : ViewModel() {

    private val _state = MutableStateFlow(StudentState())
    val state: StateFlow<StudentState> = _state

    fun loadStudent(id: Int) {

        viewModelScope.launch {

            _state.value = StudentState(loading = true)

            try {
                val student = RetrofitClient.apiService.getStudent(id)

                _state.value = StudentState(student = student)

            } catch (e: Exception) {
                _state.value = StudentState(error = "Помилка студента")
            }
        }
    }
}