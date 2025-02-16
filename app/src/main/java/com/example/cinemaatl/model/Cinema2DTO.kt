package com.example.cinemaatl.model

import com.google.gson.annotations.SerializedName

data class Cinema2DTO(
    val results: List<Result>
)

data class Result (
    @SerializedName("genre_ids")
    val genreIDS: List<Long>,
    val id: Long,



    @SerializedName("original_title")
    val name: String,

    @SerializedName("overview")
    val description: String,


    @SerializedName("poster_path")
    val poster: String,



    @SerializedName("vote_average")
    val voteAverage: Double,
)


