package com.tngblt.playlistr.models.spotifyData.playlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Image {

    @SerializedName("height")
    @Expose
    var height: Any? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("width")
    @Expose
    var width: Any? = null

}
