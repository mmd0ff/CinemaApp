package com.example.cinemaatl.model


import com.google.gson.annotations.SerializedName

data class CinemaDTO(
    val docs: List<Doc>? = null

)

data class MovieDetailDTO(
    val id: String? = null,
    val persons: List<Person>? = null, // Для getActorByID

)

data class Doc(
    val id: String? = null,
    val name: String? = null,
    val year: String? = null,
    val movieLength: String? = null,
    val genres: List<Genres>? = null,
    val persons: List<Person>? = null,
    val rating: Ratings? = null,
    @SerializedName("description")
    val description: String? = null,
    val poster: Poster? = null,
    var isTopMovie: Boolean = false
)

data class Person(
    val id: String? = null,
    val photo: String? = null,
    val name: String? = null
)

data class Ratings(
    val imdb: String
)


data class Genres(
    val name: String
)

data class Poster(
    @SerializedName("url")
    val url: String
)


