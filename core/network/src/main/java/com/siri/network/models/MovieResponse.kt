package com.siri.network.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("dates")
    val dates: MovieDate? = null,

    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("results")
    val results: List<MovieModel> = listOf(),

    @SerializedName("total_pages")
    val totalPages: Int = 1,

    @SerializedName("total_results")
    val totalResults: Int = 0,
)

data class MovieDate(
    @SerializedName("maximum")
    val maximum: String? = null,

    @SerializedName("minimum")
    val minimum: String? = null,
)