package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.artprogressmobile.ui.LoginScreen
import com.example.artprogressmobile.ui.StudentScreen
import com.example.artprogressmobile.ui.ParentScreen
import com.example.artprogressmobile.ui.TeacherScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen { role, id ->

                // перехід залежно від ролі
                when (role) {
                    "student" -> navController.navigate("student/$id")
                    "parent" -> navController.navigate("parent/$id")
                    "teacher" -> navController.navigate("teacher/$id")
                }
            }
        }

        composable(
            "student/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            StudentScreen(
                studentId = id,
                onBack = { navController.popBackStack() }  // повернення назад
            )
        }

        composable(
            "parent/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0

            ParentScreen(
                parentId = id,
                onBack = { navController.popBackStack() }    // повернення назад
            )
        }

        composable(
            "teacher/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            TeacherScreen(
                teacherId = id,
                onBack = { navController.popBackStack() }    //повернення назад
            )
        }
    }
}