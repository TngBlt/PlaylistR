package com.tngblt.playlistr.models.spotifyData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExternalIds {

    @SerializedName("isrc")
    @Expose
    var isrc: String? = null
}