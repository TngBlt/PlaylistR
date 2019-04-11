package com.tngblt.playlistr.models.spotifyData.playlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeneralPlaylistInfo {
    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("items")
    @Expose
    var playlists: List<Playlist>? = null
}