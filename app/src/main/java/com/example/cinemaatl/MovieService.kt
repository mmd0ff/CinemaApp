package com.example.cinemaatl

import com.example.cinemaatl.model.CinemaDTO
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("v1.4/movie")
    suspend fun get250TopMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("lists") lists: String = "top250",
        @Query("sortField") sortField: String = "top250",
        @Query("sortType") sortType: String = "1",
//        @Query("selectFields") selectFields: String = "id,name,description",

    ):retrofit2.Response<CinemaDTO>

    @GET("v1.4/movie")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int =1,
        @Query("limit") limit: Int =10,
        @Query("lists") lists: String = "planned-to-watch-films"
    ):retrofit2.Response<CinemaDTO>

    @GET("v1.4/movie/search")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
    ) : retrofit2.Response<CinemaDTO>




}