package com.siri.network.models

data class PopularResponse(
    val page: Int,
    val results: List<MovieModel>
)