package com.example.clicktopost.ui.screens.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.clicktopost.R
import com.example.clicktopost.ui.components.LoadingComponent
import com.example.clicktopost.ui.screens.auth.components.LoginForm
import com.example.clicktopost.ui.screens.auth.components.SignUpForm
import com.example.clicktopost.ui.theme.Dangrek

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    navHostController: NavHostController
) {
    var isLoading by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem.LoginScreen() {
            LoginForm(
                navController = navHostController,
                scaffoldState = scaffoldState,
                setIsLoading = { isLoading = it }
            )
        },
        TabItem.SignUpScreen() {
            SignUpForm(
                scaffoldState = scaffoldState,
                setIsLoading = { isLoading = it }
            )
        }
    )

    Scaffold(scaffoldState = scaffoldState) {
        Box(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Image(
                painterResource(id = R.drawable.auth_bg),
                contentDescription = "",
                modifier = Modifier.height(230.dp),
                contentScale = ContentScale.FillBounds
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "ClickToPost",
                    style = MaterialTheme.typography.h5.copy(
                        fontSize = 56.sp,
                        color = Color.White
                    ),
                    fontFamily = Dangrek,
                    modifier = Modifier.padding(top = 50.dp, bottom = 25.dp)
                )
                Surface(
                    elevation = 4.dp,
                    modifier = Modifier.padding(horizontal = 30.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            backgroundColor = Color.Transparent,
                            contentColor = MaterialTheme.colors.primary
                        ) {
                            tabs.forEachIndexed { index, tabItem ->
                                Tab(
                                    modifier = Modifier.height(48.dp),
                                    selected = index == selectedTab,
                                    selectedContentColor = MaterialTheme.colors.primary,
                                    unselectedContentColor = MaterialTheme.colors.primary.copy(
                                        alpha = 0.5f
                                    ),
                                    onClick = { selectedTab = index }
                                ) {
                                    Text(
                                        text = tabItem.title,
                                        style = MaterialTheme.typography.h6
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        tabs[selectedTab].composable()
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
    LoadingComponent(visibility = isLoading)
}