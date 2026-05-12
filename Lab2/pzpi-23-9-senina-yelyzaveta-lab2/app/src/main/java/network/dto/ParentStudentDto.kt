package com.example.artprogressmobile.network.dto

data class ParentStudentDto(
    val parentID: Int,
    val studentID: Int,
    val student: StudentDto? = null
)