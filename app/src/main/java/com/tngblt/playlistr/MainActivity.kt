package com.tngblt.playlistr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.SpotifyAppRemote

import com.spotify.protocol.types.Track
import com.google.firebase.firestore.FirebaseFirestore
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import android.content.Intent
import android.os.Debug
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.LruCache
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.auth.User
import com.tngblt.playlistr.userdata.UserPlaylist
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val requestCode = 1997
    private val clientId = "148896cafca44560a3f8f3c7031a50d0"
    private val redirectUri = "https://com.tngblt.playlistr/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    var db = FirebaseFirestore.getInstance()

    private var textViewResult:TextView? = null
    private var userImage:ImageView? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var currentUserPlaylist: ArrayList<UserPlaylist> = arrayListOf(UserPlaylist("","","",false))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = DataAdaptaterRecycler(currentUserPlaylist)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    override fun onStart() {
        super.onStart()

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        val builder = AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri)
        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, requestCode, request)

        textViewResult = findViewById(R.id.text_view_result)
        userImage = findViewById(R.id.userImage)
        // Create a new user with a first and last name FIRESTORE
        /*
        val user = HashMap<String,Any>()
        user.put("first", "Ada")
        user.put("last", "Lovelace")
        user.put("born", 1815)

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "MainActivity",
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("MainActivity", "Error adding document", e) }
            */
    }

    private fun connected() {
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("MainActivity", track.name + " by " + track.artist.name)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }


    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (reqCode == requestCode) {
            val response = AuthenticationClient.getResponse(resultCode, intent)

            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {

                    UserRequest(OkHttpClient(), response.accessToken)
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                    Log.d("MainActivity", "Error : " + response.error)
                }
                else -> {
                    Log.d("MainActivity", "Error : The auth flow must have been cancelled")
                }
            }
        }
    }

    fun UserRequest(client: OkHttpClient, accessToken:String) {
        val apiUrl = "https://api.spotify.com/v1/me"
        val request = Request.Builder()
            .url(apiUrl )
            .get()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful) {
                        var res = response.body()?.string().toString()
                        var resJson = JSONObject(res)
                        print(res)
                        this@MainActivity.runOnUiThread {
                            textViewResult?.text = resJson["display_name"].toString()
                            val imageUrl = JSONObject(resJson.getJSONArray("images")[0].toString())["url"].toString()

                            DownloadImageTask(userImage)
                                .execute(ImageParams(imageUrl, null, "userAvatar"))
                            getUserPlaylists(client,accessToken)
                        }
                    }
                }
            })
    }

    fun getUserPlaylists(client: OkHttpClient, accessToken:String) {
        val apiUrl = "https://api.spotify.com/v1/me/playlists?limit=50&offset=0"
        val request = Request.Builder()
            .url(apiUrl )
            .get()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Debug.waitForDebugger()
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful) {
                        var res = response.body()?.string().toString()
                        var resJson = JSONObject(res)
                        var playlists = resJson.getJSONArray("items")
                        currentUserPlaylist.clear()
                        for (i in 0..(playlists.length() - 1)) {
                            val pl = JSONObject(playlists.getJSONObject(i).toString())

                            val userPlaylist = UserPlaylist(pl["name"].toString(),
                                pl["id"].toString(),
                                JSONObject(pl.getJSONArray("images")[0].toString())["url"].toString(),
                                pl["public"].toString().toBoolean())

                            currentUserPlaylist.add(userPlaylist)
                        }
                        this@MainActivity.runOnUiThread {
                            viewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }
}
