package com.tngblt.playlistr.models.spotifyData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListAlbums {

    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("items")
    @Expose
    var items: List<Album>? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("next")
    @Expose
    var next: Any? = null

    @SerializedName("offset")
    @Expose
    var offset: Int? = null

    @SerializedName("previous")
    @Expose
    var previous: Any? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null
}
