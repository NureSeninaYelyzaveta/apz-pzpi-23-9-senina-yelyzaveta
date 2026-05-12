package com.example.artprogressmobile.network

import com.example.artprogressmobile.network.dto.GradeDto
import com.example.artprogressmobile.network.dto.ParentDto
import com.example.artprogressmobile.network.dto.StudentDto
import com.example.artprogressmobile.network.dto.TeacherDto
import com.example.artprogressmobile.network.dto.ParentStudentDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Авторизація студента по email
    @GET("api/Students/byEmail")
    suspend fun loginStudent(
        @Query("email") email: String
    ): StudentDto

    // Отримання даних студента по id
    @GET("api/Students/{id}")
    suspend fun getStudent(
        @Path("id") id: Int
    ): StudentDto

    // Пошук батьків по email
    @GET("api/Parents/byEmail")
    suspend fun getParentByEmail(
        @Query("email") email: String
    ): ParentDto

    // Отримання повної інформації про батьків
    @GET("api/Parents/{id}")
    suspend fun getParentFull(
        @Path("id") id: Int
    ): ParentDto

    // Пошук викладача по email
    @GET("api/Teachers/byEmail")
    suspend fun getTeacherByEmail(
        @Query("email") email: String
    ): TeacherDto

    // Отримання даних викладача по id
    @GET("api/Teachers/{id}")
    suspend fun getTeacher(
        @Path("id") id: Int
    ): TeacherDto

    // Отримання списку зв’язків батьків і студентів
    @GET("api/ParentStudents")
    suspend fun getParentStudents(): List<ParentStudentDto>

    // Отримання оцінок викладача
    @GET("api/Grades/byTeacher/{teacherId}")
    suspend fun getGradesByTeacher(
        @Path("teacherId") teacherId: Int
    ): List<GradeDto>


}