package com.tngblt.playlistr.models.spotifyData.user

import com.google.gson.annotations.SerializedName


data class Followers(

    @SerializedName("href")
    val href: String?, // null

    @SerializedName("total")
    val total: Int // 3
)
