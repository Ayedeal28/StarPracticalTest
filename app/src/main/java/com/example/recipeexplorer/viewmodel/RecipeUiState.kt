package com.example.recipeexplorer.viewmodel

import com.example.recipeexplorer.data.model.Recipe

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Recipe>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}