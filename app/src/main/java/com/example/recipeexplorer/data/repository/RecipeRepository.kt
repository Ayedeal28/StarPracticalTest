package com.example.recipeexplorer.data.repository

import com.example.recipeexplorer.data.model.Recipe
import com.example.recipeexplorer.data.model.RecipeResponse
import com.example.recipeexplorer.data.remote.RetrofitInstance
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RecipeRepository {

    private val api = RetrofitInstance.api

    suspend fun getRecipes(limit: Int = 10, skip: Int = 0): Result<RecipeResponse> {
        return try {
            val response = api.getRecipes(limit, skip)
            Result.success(response)
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Request timed out. Please try again."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Something went wrong: ${e.localizedMessage}"))
        }
    }
    suspend fun searchRecipes(query: String, limit: Int = 10, skip: Int = 0): Result<RecipeResponse> {
        return try {
            val response = api.searchRecipes(query, limit, skip)
            Result.success(response)
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Search timed out. Please try again."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Search failed: ${e.localizedMessage}"))
        }
    }

    suspend fun getRecipeById(id: Int): Result<Recipe> {
        return try {
            val response = api.getRecipeById(id)
            Result.success(response)
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Request timed out. Please try again."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load recipe: ${e.localizedMessage}"))
        }
    }
}