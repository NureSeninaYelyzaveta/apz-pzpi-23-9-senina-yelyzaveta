package com.example.artprogressmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artprogressmobile.network.RetrofitClient
import com.example.artprogressmobile.network.dto.StudentDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ParentState(
    val loading: Boolean = false,
    val error: String? = null,
    val students: List<StudentDto> = emptyList()
)

class ParentViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        ParentState()
    )

    val state: StateFlow<ParentState> = _state

    // Завантаження даних батьків і їх дітей
    fun loadParentData(parentId: Int) {

        viewModelScope.launch {

            _state.value = ParentState(
                loading = true
            )

            try {

                // звязок батько-студент
                val relations =
                    RetrofitClient.apiService.getParentStudents()

                // ID дітей цього батька
                val studentIds = relations
                    .filter { it.parentID == parentId }
                    .map { it.studentID }


                val students = studentIds.map { studentId ->

                    async {
                        RetrofitClient.apiService.getStudent(studentId)
                    }
                }.awaitAll()

                _state.value = ParentState(
                    loading = false,
                    students = students
                )

            } catch (e: Exception) {

                _state.value = ParentState(
                    loading = false,
                    error = e.message ?: "Помилка завантаження"
                )
            }
        }
    }
}