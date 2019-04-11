package com.tngblt.playlistr.models.spotifyData.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tngblt.playlistr.models.spotifyData.ListAlbums
import com.tngblt.playlistr.models.spotifyData.ListArtists
import com.tngblt.playlistr.models.spotifyData.ListPlaylists
import com.tngblt.playlistr.models.spotifyData.ListTracks

class GeneralSearchInfo {
    @SerializedName("playlists")
    @Expose
    var playlists: ListPlaylists? = null

    @SerializedName("artists")
    @Expose
    var artists: ListArtists? = null

    @SerializedName("albums")
    @Expose
    var albums: ListAlbums? = null

    @SerializedName("tracks")
    @Expose
    var tracks: ListTracks? = null
}