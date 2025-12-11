package com.example.recipeexplorer.data.remote

import com.example.recipeexplorer.data.model.RecipeResponse
import com.example.recipeexplorer.data.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("recipes")
    suspend fun getRecipes(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): RecipeResponse

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): RecipeResponse

    @GET("recipes/{id}")
    suspend fun getRecipeById(
        @Path("id") id: Int
    ): Recipe
}