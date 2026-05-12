package com.example.artprogressmobile.network

sealed class LoginResult {
    data class Student(val id: Int) : LoginResult()   // Успіх студента
    data class Parent(val id: Int) : LoginResult()     // Успіх батька
    data class Teacher(val id: Int) : LoginResult()    // Успіх вчителя
    data object Error : LoginResult()          // Помилка
}