package com.example.cinemaatl.model


import com.google.gson.annotations.SerializedName

data class CinemaDTO(
    val docs: List<Doc>

)

data class Doc(
    val id:String,
    val name:String,
    val year:String,
    val movieLength:String,
    val genres:List<Genres>,
    val persons: List<Person>,
    val rating:Ratings,
    @SerializedName("description")
    val description: String?,
    val poster:Poster,
    val isTopMovie:Boolean = false


    )

data class Person(
    val id:String,
    val photo: String,
    val name:String
)
data class Ratings(
    val imdb:String
)

data class Genres(
    val name:String
)

data class Poster(
    @SerializedName("url")
    val url:String
)


