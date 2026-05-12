package com.example.artprogressmobile.viewmodel

import com.example.artprogressmobile.network.dto.StudentDto
import com.example.artprogressmobile.network.dto.ParentDto

data class AppState(
    val loading: Boolean = false,     // Стан завантаження
    val error: String? = null,    //помилка

    val parent: ParentDto? = null,
    val students: List<StudentDto> = emptyList()
)