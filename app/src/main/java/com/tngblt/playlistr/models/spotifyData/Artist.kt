package com.tngblt.playlistr.models.spotifyData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.playlist.ExternalUrl
import com.tngblt.playlistr.models.spotifyData.playlist.Image
import com.tngblt.playlistr.models.spotifyData.user.Followers

class Artist {

    @SerializedName("external_urls")
    @Expose
    var externalUrl: ExternalUrl? = null
        private set

    @SerializedName("followers")
    @Expose
    var followers: Followers? = null

    @SerializedName("genres")
    @Expose
    var genres: List<String>? = null

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

    @SerializedName("popularity")
    @Expose
    var popularity: Int? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("uri")
    @Expose
    var uri: String? = null

    fun setExternalUrls(externalUrl: ExternalUrl) {
        this.externalUrl = externalUrl
    }

}
