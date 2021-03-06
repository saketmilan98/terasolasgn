package com.terasolasgn.movieinfo.model

import java.io.Serializable

class MovieDataClass : ArrayList<MovieDataClassItem>()

data class MovieDataClassItem(
    val info: MovieInfo,
    val title: String,
    val year: Int
) : Serializable

data class MovieInfo(
    val actors: List<String>?,
    val directors: List<String>?,
    val genres: List<String>?,
    val image_url: String?,
    val plot: String?,
    val rank: Int?,
    val rating: Double?,
    val release_date: String?,
    val running_time_secs: Int
) : Serializable