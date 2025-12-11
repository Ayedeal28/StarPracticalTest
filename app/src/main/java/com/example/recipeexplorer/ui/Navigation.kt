package com.example.recipeexplorer.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipeexplorer.ui.screens.RecipeDetailScreen
import com.example.recipeexplorer.ui.screens.RecipeListScreen
import com.example.recipeexplorer.viewmodel.RecipeViewModel

@Composable
fun RecipeNavigation(viewModel: RecipeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "recipe_list"
    ) {
        composable("recipe_list") {
            RecipeListScreen(
                viewModel = viewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                }
            )
        }

        composable(
            route = "recipe_detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            RecipeDetailScreen(
                recipeId = recipeId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}