package com.compose.watchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.watchlist.domain.enums.EnumDestination
import com.compose.watchlist.ui.screens.MovieDetailScreen
import com.compose.watchlist.ui.screens.SearchScreen
import com.compose.watchlist.ui.screens.WatchListScreen
import com.compose.watchlist.ui.theme.WatchListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WatchListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavBar(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavBar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = EnumDestination.Search
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Scaffold(modifier = modifier,
        bottomBar = {
            if (currentRoute in EnumDestination.entries.map { it.route }) {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    EnumDestination.entries.forEachIndexed { index, enumDestination ->
                        NavigationBarItem(
                            selected = selectedDestination == index,
                            onClick = {
                                navController.navigate(route = enumDestination.route)
                                selectedDestination = index
                            },
                            icon = {
                                Icon(enumDestination.icon, enumDestination.description)
                            },
                            label = { Text(enumDestination.label) }
                        )
                    }
                }
            }

        }

    ) { paddingValues ->
        AppNavHost(modifier = modifier.padding(paddingValues), navController, startDestination)
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: EnumDestination
) {
    NavHost(navController = navController, startDestination = startDestination.route) {
        EnumDestination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    EnumDestination.Search -> SearchScreen(
                        modifier = modifier,
                        navController = navController
                    )

                    EnumDestination.WatchList -> WatchListScreen(
                        modifier = modifier
                    )
                }
            }

        }
        composable(
            route = "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            MovieDetailScreen(movieId = movieId, navController = navController)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    WatchListTheme {
        NavBar(modifier = Modifier)
    }
}