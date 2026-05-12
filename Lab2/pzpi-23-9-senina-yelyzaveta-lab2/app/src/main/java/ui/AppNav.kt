package com.example.artprogressmobile.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNav(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onNavigate = { role, id ->

                    // перехід залежно від ролі
                    when (role) {
                        "student" -> navController.navigate("student/$id")
                        "parent" -> navController.navigate("parent/$id")
                        "teacher" -> navController.navigate("teacher/$id")
                    }
                }
            )
        }

        composable("parent/{id}") { backStack ->
            //отримання ід батьків
            val id = backStack.arguments?.getString("id")?.toInt() ?: 0

            ParentScreen(
                parentId = id,
                // повернення на попереднвй екран
                onBack = { navController.popBackStack() }
            )
        }
    }
}