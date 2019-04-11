package com.tngblt.playlistr.models.spotifyData.playlist


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.Album
import com.tngblt.playlistr.models.spotifyData.ArtistLight
import com.tngblt.playlistr.models.spotifyData.ExternalIds

class Track {

    @SerializedName("album")
    @Expose
    var album:Album? = null

    @SerializedName("artists")
    @Expose
    var artists:List<ArtistLight>? = null

    @SerializedName("available_markets")
    @Expose
    var available_markets:List<String>? = null

    @SerializedName("disc_number")
    @Expose
    var disc_number:Int? = null

    @SerializedName("duration_ms")
    @Expose
    var duration_ms:Int? = null

    @SerializedName("explicit")
    @Expose
    var explicit:Boolean? = null

    @SerializedName("external_ids")
    @Expose
    var external_ids: ExternalIds? = null

    @SerializedName("external_urls")
    @Expose
    var external_urls:ExternalUrl? = null

    @SerializedName("href")
    @Expose
    var href:String? = null

    @SerializedName("id")
    @Expose
    var id:String? = null

    @SerializedName("is_local")
    @Expose
    var is_local:Boolean? = null

    @SerializedName("name")
    @Expose
    var name:String? = null

    @SerializedName("popularity")
    @Expose
    var popularity:Int? = null

    @SerializedName("preview_url")
    @Expose
    var preview_url:String? = null

    @SerializedName("track_number")
    @Expose
    var track_number:Int? = null

    @SerializedName("type")
    @Expose
    var type:String? = null

    @SerializedName("uri")
    @Expose
    var uri:String? = null

}