package com.example.artprogressmobile.network.dto

data class StudentDto(
    val studentID: Int,
    val name: String,
    val group: String?,
    val contactInfo: String,
    val grades: List<GradeDto>
)