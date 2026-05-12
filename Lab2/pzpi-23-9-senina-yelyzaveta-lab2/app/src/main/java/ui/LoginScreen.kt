package com.example.artprogressmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.artprogressmobile.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigate: (String, Int) -> Unit
) {
    val scope = rememberCoroutineScope()  // для роботи з запитами

    var email by remember { mutableStateOf("") }  // змінна для емейл
    var role by remember { mutableStateOf("student") }   // роль користувача
    var error by remember { mutableStateOf<String?>(null) }  // помилка
    var loading by remember { mutableStateOf(false) }    // стан завантаження

    val primaryGradient = Brush.verticalGradient(   //фон
        colors = listOf(
            Color(0xFFF5F7FA),
            Color(0xFFECEFFF)
        )
    )

    val brandGradient = Brush.horizontalGradient(   //лого
        colors = listOf(
            Color(0xFF667EEA),
            Color(0xFF764BA2)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryGradient),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // назва додатку
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brandGradient)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ArtProgress",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // заголовок входу
                Text(
                    "Вхід",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // поле для емейлу
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        error = null
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Оберіть роль", fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(8.dp))

                // вибір ролі
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RoleChip("student", "Учень", role) { role = it }
                    RoleChip("parent", "Батьки", role) { role = it }
                    RoleChip("teacher", "Викладач", role) { role = it }
                }

                Spacer(modifier = Modifier.height(20.dp))


                // кнопка входу
                Button(
                    onClick = {
                        scope.launch {
                            loading = true
                            error = null

                            try {
                                // перевірка ролі користувача
                                when (role) {

                                    "student" -> {
                                        val student = RetrofitClient.apiService.loginStudent(email)
                                        onNavigate("student", student.studentID)
                                    }

                                    "parent" -> {
                                        val parent = RetrofitClient.apiService.getParentByEmail(email)
                                        onNavigate("parent", parent.parentID)
                                    }

                                    "teacher" -> {
                                        val teacher = RetrofitClient.apiService.getTeacherByEmail(email)
                                        onNavigate("teacher", teacher.teacherID)
                                    }
                                }

                            } catch (e: Exception) {
                                error = e.message ?: "Помилка з'єднання"
                            }

                            loading = false
                        }
                    },
                    enabled = !loading && email.isNotBlank(),   // кнопка активна лише коли введено емейл
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667EEA)
                    )
                ) {
                    Text(
                        if (loading) "Завантаження..." else "Увійти",
                        color = Color.White
                    )
                }

                error?.let {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun RoleChip(
    value: String,
    label: String,
    selected: String,
    onSelect: (String) -> Unit
) {
    // перевірка чи обрано роль
    val isSelected = value == selected

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Color(0xFF667EEA) else Color(0xFFE0E0E0)
            )
            .clickable { onSelect(value) }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

