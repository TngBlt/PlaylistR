package com.tngblt.playlistr.models.spotifyData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.playlist.ExternalUrl
import com.tngblt.playlistr.models.spotifyData.playlist.Image

class Album {

    @SerializedName("album_type")
    @Expose
    var albumType: String? = null

    @SerializedName("artists")
    @Expose
    var artists: List<ArtistLight>? = null

    @SerializedName("external_urls")
    @Expose
    var externalUrls: ExternalUrl? = null

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

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    @SerializedName("release_date_precision")
    @Expose
    var releaseDatePrecision: String? = null

    @SerializedName("total_tracks")
    @Expose
    var totalTracks: Int? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("uri")
    @Expose
    var uri: String? = null

}
