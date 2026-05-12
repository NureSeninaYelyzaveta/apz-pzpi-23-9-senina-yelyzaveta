package com.example.artprogressmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Groups
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
import com.example.artprogressmobile.viewmodel.ParentViewModel

@Composable
fun ParentScreen(
    parentId: Int,
    onBack: () -> Unit,
    viewModel: ParentViewModel = viewModel()
) {

    // Отримання стану з ViewModel
    val state by viewModel.state.collectAsState()

    // Завантаження даних батьків
    LaunchedEffect(parentId) {
        viewModel.loadParentData(parentId)
    }

    val backgroundGradient = Brush.verticalGradient(  //фон
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

        when {

            state.loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = Color(0xFF667EEA)
                    )
                }
            }

            state.error != null -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {

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
                                text = "Кабінет батьків",
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        // Виведення списку студентів
                        items(state.students) { student ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Box(    // Іконка студента
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
                                                text = student.name,
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = "Група: ${student.group ?: "-"}",
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Groups,
                                            contentDescription = null,
                                            tint = Color(0xFF667EEA)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = "Оцінки",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Перевірка чи є оцінки
                                    if (student.grades.isEmpty()) {

                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFF8F8F8)
                                            ),
                                            shape = RoundedCornerShape(18.dp)
                                        ) {

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(20.dp),
                                                contentAlignment = Alignment.Center
                                            ) {

                                                Text(
                                                    text = "Оцінок поки немає",
                                                    color = Color.Gray
                                                )
                                            }
                                        }

                                    } else {

                                        // Виведення оцінок
                                        student.grades.forEach { grade ->

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 12.dp),
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color(0xFFFDFDFF)
                                                ),
                                                elevation = CardDefaults.cardElevation(4.dp)
                                            ) {

                                                Column(
                                                    modifier = Modifier.padding(18.dp)
                                                ) {

                                                    Row(
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {

                                                        Column(
                                                            modifier = Modifier.weight(1f)
                                                        ) {

                                                            Text(
                                                                text = grade.disciplineName,
                                                                style = MaterialTheme.typography.titleMedium,
                                                                fontWeight = FontWeight.Bold
                                                            )

                                                            Spacer(modifier = Modifier.height(6.dp))

                                                            Text(
                                                                text = "Викладач: ${grade.teacherName}",
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

                                                    Spacer(modifier = Modifier.height(10.dp))

                                                    Text(
                                                        text = "Дата: ${grade.date}",
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Кнопка повернення
                        item {
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
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}
