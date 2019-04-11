package com.tngblt.playlistr.models.spotifyData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.playlist.Playlist
import com.tngblt.playlistr.models.spotifyData.playlist.Track

class ListPlaylists {

    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("items")
    @Expose
    var items: List<Playlist>? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("next")
    @Expose
    var next: String? = null

    @SerializedName("offset")
    @Expose
    var offset: Int? = null

    @SerializedName("previous")
    @Expose
    var previous: String? = null

}
