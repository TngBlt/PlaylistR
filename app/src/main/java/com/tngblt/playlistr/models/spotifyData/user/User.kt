package com.tngblt.playlistr.models.spotifyData.user


import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.playlist.ExternalUrl
import com.tngblt.playlistr.models.spotifyData.playlist.Image

data class User(
    @SerializedName("display_name")
    val displayName: String, // tanguy21b
    @SerializedName("external_urls")
    val externalUrls: ExternalUrl,
    @SerializedName("followers")
    val followers: Followers,
    @SerializedName("href")
    val href: String, // https://api.spotify.com/v1/users/tanguy21b
    @SerializedName("id")
    val id: String, // tanguy21b
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("type")
    val type: String, // user
    @SerializedName("uri")
    val uri: String // spotify:user:tanguy21b
)
