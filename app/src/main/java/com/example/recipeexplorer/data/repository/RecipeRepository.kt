package com.example.recipeexplorer.data.repository

import com.example.recipeexplorer.data.model.Recipe
import com.example.recipeexplorer.data.model.RecipeResponse
import com.example.recipeexplorer.data.remote.RetrofitInstance

class RecipeRepository {

    private val api = RetrofitInstance.api

    suspend fun getRecipes(limit: Int = 10, skip: Int = 0): Result<RecipeResponse> {
        return try {
            val response = api.getRecipes(limit, skip)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchRecipes(query: String, limit: Int = 10, skip: Int = 0): Result<RecipeResponse> {
        return try {
            val response = api.searchRecipes(query, limit, skip)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeById(id: Int): Result<Recipe> {
        return try {
            val response = api.getRecipeById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}