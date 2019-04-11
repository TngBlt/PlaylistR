package com.tngblt.playlistr.models.spotifyData.playlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Owner {

    @SerializedName("display_name")
    @Expose
    var displayName: String? = null

    @SerializedName("external_urls")
    @Expose
    var externalUrl: ExternalUrl? = null

    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("uri")
    @Expose
    var uri: String? = null

}
