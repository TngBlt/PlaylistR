package com.tngblt.playlistr.models.spotifyData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.playlist.ExternalUrl

class ArtistLight {

    @SerializedName("external_urls")
    @Expose
    var external_urls:ExternalUrl? = null

    @SerializedName("href")
    @Expose
    var href:String? = null

    @SerializedName("id")
    @Expose
    var id:String? = null

    @SerializedName("name")
    @Expose
    var name:String? = null

    @SerializedName("type")
    @Expose
    var type:String? = null

    @SerializedName("uri")
    @Expose
    var uri:String? = null
}
