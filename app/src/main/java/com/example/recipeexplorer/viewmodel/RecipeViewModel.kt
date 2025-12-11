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

    // ✨ ADD THESE NEW PROPERTIES:
    private var totalRecipes = 0
    private var hasMoreRecipes = true

    init {
        loadRecipes()
    }

    fun shouldShowLoadMore(): Boolean {
        return hasMoreRecipes && allRecipes.size < totalRecipes
    }

    fun loadRecipes(refresh: Boolean = false) {
        if (refresh) {
            currentSkip = 0
            allRecipes = emptyList()
            hasMoreRecipes = true
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
                    totalRecipes = response.total

                    allRecipes = if (refresh) {
                        response.recipes
                    } else {
                        allRecipes + response.recipes
                    }

                    hasMoreRecipes = allRecipes.size < totalRecipes

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
        if (isLoadingMore || !hasMoreRecipes) return

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
                    totalRecipes = response.total

                    allRecipes = allRecipes + response.recipes

                    hasMoreRecipes = allRecipes.size < totalRecipes

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
        hasMoreRecipes = true
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