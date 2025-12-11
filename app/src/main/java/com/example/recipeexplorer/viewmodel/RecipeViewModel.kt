package com.example.recipeexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeexplorer.data.model.Recipe
import com.example.recipeexplorer.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val repository = RecipeRepository()

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private var allRecipes: List<Recipe> = emptyList()

    private var currentSkip = 0
    private val pageSize = 10
    private var isLoadingMore = false

    private var currentSearchQuery: String = ""

    private var currentDifficultyFilter: String? = null

    init {
        loadRecipes()
    }

    fun loadRecipes(refresh: Boolean = false) {
        if (refresh) {
            currentSkip = 0
            allRecipes = emptyList()
        }

        viewModelScope.launch {
            _uiState.value = RecipeUiState.Loading

            val result = if (currentSearchQuery.isNotEmpty()) {
                repository.searchRecipes(currentSearchQuery, pageSize, currentSkip)
            } else {
                repository.getRecipes(pageSize, currentSkip)
            }

            result.fold(
                onSuccess = { response ->
                    allRecipes = if (refresh) {
                        response.recipes
                    } else {
                        allRecipes + response.recipes
                    }
                    applyFilter()
                },
                onFailure = { exception ->
                    _uiState.value = RecipeUiState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    fun loadMoreRecipes() {
        if (isLoadingMore) return

        isLoadingMore = true
        currentSkip += pageSize

        viewModelScope.launch {
            val result = if (currentSearchQuery.isNotEmpty()) {
                repository.searchRecipes(currentSearchQuery, pageSize, currentSkip)
            } else {
                repository.getRecipes(pageSize, currentSkip)
            }

            result.fold(
                onSuccess = { response ->
                    allRecipes = allRecipes + response.recipes
                    applyFilter()
                    isLoadingMore = false
                },
                onFailure = { exception ->
                    _uiState.value = RecipeUiState.Error(
                        exception.message ?: "Failed to load more recipes"
                    )
                    isLoadingMore = false
                }
            )
        }
    }

    fun searchRecipes(query: String) {
        currentSearchQuery = query
        currentSkip = 0
        allRecipes = emptyList()
        loadRecipes()
    }

    fun filterByDifficulty(difficulty: String?) {
        currentDifficultyFilter = difficulty
        applyFilter()
    }

    private fun applyFilter() {
        val filteredRecipes = if (currentDifficultyFilter != null) {
            allRecipes.filter { it.difficulty.equals(currentDifficultyFilter, ignoreCase = true) }
        } else {
            allRecipes
        }

        _uiState.value = RecipeUiState.Success(filteredRecipes)
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        val result = repository.getRecipeById(id)
        return result.getOrNull()
    }
}