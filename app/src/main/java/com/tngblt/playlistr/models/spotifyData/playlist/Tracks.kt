package com.tngblt.playlistr.models.spotifyData.playlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tracks {

    @SerializedName("href")
    @Expose
    var href: String? = null
    @SerializedName("total")
    @Expose
    var total: Int? = null

}
