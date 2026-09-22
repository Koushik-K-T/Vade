package com.example.vade.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vade.today.ui.TodayScreen
import com.example.vade.tasks.ui.TasksScreen
import com.example.vade.tasks.ui.TaskEditorSheet

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Today : Screen("today", "Today", Icons.Default.Home)
    object Tasks : Screen("tasks", "Tasks", Icons.Default.CheckCircle)
    object Notes : Screen("notes", "Notes", Icons.AutoMirrored.Filled.List)
    object Habits : Screen("habits", "Habits", Icons.Default.DateRange)
    object Money : Screen("money", "Money", Icons.Default.DateRange)
}

val items = listOf(
    Screen.Today,
    Screen.Tasks,
    Screen.Notes,
    Screen.Habits,
    Screen.Money
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VadeApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    var showQuickAdd by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showQuickAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Quick Add")
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Today.route, Modifier.padding(innerPadding)) {
            composable(Screen.Today.route) { 
                TodayScreen(
                    onStartReview = { navController.navigate("review") }
                ) 
            }
            composable(Screen.Tasks.route) { TasksScreen() }
            composable(Screen.Notes.route) { 
                com.example.vade.notes.ui.NotesScreen(
                    onStartReview = { 
                        navController.navigate("review")
                    }
                ) 
            }
            composable(Screen.Habits.route) { com.example.vade.habits.ui.HabitsScreen() }
            composable(Screen.Money.route) { com.example.vade.money.ui.MoneyScreen() }
            composable("review") {
                com.example.vade.notes.ui.ReviewScreen(
                    onFinish = { navController.popBackStack() }
                )
            }
        }
        
        if (showQuickAdd) {
            TaskEditorSheet(onDismiss = { showQuickAdd = false })
        }
    }
}
