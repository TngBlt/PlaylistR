package com.tngblt.playlistr.models.spotifyData.playlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Playlist {

    @SerializedName("collaborative")
    @Expose
    var collaborative: Boolean? = null
    @SerializedName("external_urls")
    @Expose
    var externalUrl: ExternalUrl? = null
    @SerializedName("href")
    @Expose
    var href: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("images")
    @Expose
    var images: List<Image>? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("owner")
    @Expose
    var owner: Owner? = null
    @SerializedName("primary_color")
    @Expose
    var primaryColor: Any? = null
    @SerializedName("public")
    @Expose
    var public: Boolean? = null
    @SerializedName("snapshot_id")
    @Expose
    var snapshotId: String? = null
    @SerializedName("tracks")
    @Expose
    var tracks: Tracks? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("uri")
    @Expose
    var uri: String? = null

}
