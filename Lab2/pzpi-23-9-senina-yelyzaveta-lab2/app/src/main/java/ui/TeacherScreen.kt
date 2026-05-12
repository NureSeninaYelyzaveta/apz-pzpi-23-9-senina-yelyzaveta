package com.example.artprogressmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artprogressmobile.viewmodel.TeacherViewModel


@Composable
fun TeacherScreen(
    teacherId: Int,
    onBack: () -> Unit,
    viewModel: TeacherViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(teacherId) {
        viewModel.loadTeacher(teacherId)
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F7FA),
            Color(0xFFECEFFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF667EEA)
                )
            }
            return
        }

        if (state.error.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            return
        }

        state.teacher?.let { t ->

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // Верхня панель
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF667EEA),
                                    Color(0xFF764BA2)
                                )
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {

                    Column {

                        Text(
                            text = "ArtProgress",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Панель викладача",
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Карточка профіля
                    item {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.95f)
                            ),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE8ECFF)),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color(0xFF667EEA),
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {

                                        Text(
                                            text = t.name,
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = t.position,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                TeacherInfoRow(
                                    title = "Email",
                                    value = t.contactInfo
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                TeacherInfoRow(
                                    title = "ID викладача",
                                    value = teacherId.toString()
                                )
                            }
                        }
                    }

                    // Заголовок оцінок
                    item {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Оцінки студентів",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF667EEA))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {

                                Text(
                                    text = "${state.grades.size}",
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Карточки оцінок
                    items(state.grades) { grade ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = grade.studentName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = grade.disciplineName,
                                            color = Color.Gray
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFF667EEA))
                                            .padding(
                                                horizontal = 16.dp,
                                                vertical = 10.dp
                                            )
                                    ) {

                                        Text(
                                            text = grade.value.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                HorizontalDivider(
                                    color = Color(0xFFEAEAEA)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Дата: ${grade.date}",
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    item {

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onBack,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF667EEA)
                            )
                        ) {

                            Text(
                                text = "Назад",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherInfoRow(
    title: String,
    value: String
) {

    Column {

        Text(
            text = title,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}



