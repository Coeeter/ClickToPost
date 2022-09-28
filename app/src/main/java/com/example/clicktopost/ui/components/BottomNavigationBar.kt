package com.example.clicktopost.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.clicktopost.ui.utils.BottomNavigationBarItem

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        BottomAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
        ) {
            val items = BottomNavigationBarItem.values()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.route,
                    selectedContentColor = MaterialTheme.colors.primary,
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}