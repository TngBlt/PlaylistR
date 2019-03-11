package com.tngblt.playlistr.models.spotifyData.playlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExternalUrl {

    @SerializedName("spotify")
    @Expose
    var spotify: String? = null

}
