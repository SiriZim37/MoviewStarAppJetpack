package com.siri.network.models

import com.google.gson.annotations.SerializedName


data class MovieModel(

    @SerializedName("adult")
    val isForAdult: Boolean = false,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int> = listOf(),

    @SerializedName("id")
    val id: Long = -1,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("original_title")
    val originalTitle: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("popularity")
    val popularity: Double = 0.0,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("video")
    val hasVideo: Boolean = false,

    @SerializedName("vote_average")
    val voteAverage: Float = 0.0f,

    @SerializedName("vote_count")
    val voteCount: Int = 0,
)