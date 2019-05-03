package com.tngblt.playlistr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tngblt.playlistr.R
import com.tngblt.playlistr.interfaces.ApiService
import com.tngblt.playlistr.models.spotifyData.playlist.Playlist
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException

class PlaylistActivity : AppCompatActivity() {

    private var currentPlaylist: Playlist? = null
    private var disposable: Disposable? = null
    private val apiService by lazy {
        ApiService.create()
    }

    private lateinit var playlistId: String
    private lateinit var apiKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        this.playlistId = savedInstanceState?.getString("playlistId") ?: throw IllegalArgumentException("PlaylistId required")
        this.apiKey = savedInstanceState.getString("apiKey") ?: throw IllegalArgumentException("apiKey required")

        loadCurrentPlaylist(this.playlistId, this.apiKey)
    }

    /**
     * Loads the playlist
     */
    private fun loadCurrentPlaylist(playlistId:String, apiKey: String) {
        disposable = apiService.getPlaylist(playlistId,apiKey).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> currentPlaylist = result},
                { error -> Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()}
            )
    }
}
