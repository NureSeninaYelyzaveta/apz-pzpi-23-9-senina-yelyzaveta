package com.example.artprogressmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artprogressmobile.network.RetrofitClient
import com.example.artprogressmobile.network.dto.GradeDto
import com.example.artprogressmobile.network.dto.TeacherDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TeacherState(
    val teacher: TeacherDto? = null,
    val grades: List<GradeDto> = emptyList(),
    val loading: Boolean = false,
    val error: String = ""
)

class TeacherViewModel : ViewModel() {

    private val _state = MutableStateFlow(TeacherState())
    val state: StateFlow<TeacherState> = _state

    fun loadTeacher(id: Int) {

        viewModelScope.launch {

            _state.value = TeacherState(loading = true)

            try {

                val teacher = RetrofitClient.apiService.getTeacher(id)

               
                val grades = RetrofitClient.apiService.getGradesByTeacher(id)

                _state.value = TeacherState(
                    teacher = teacher,
                    grades = grades,
                    loading = false
                )

            } catch (e: Exception) {
                e.printStackTrace()

                _state.value = TeacherState(
                    loading = false,
                    error = e.message ?: "Помилка викладача"
                )
            }
        }
    }
}