package com.tngblt.playlistr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.SpotifyAppRemote

import com.google.firebase.firestore.FirebaseFirestore
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import android.content.Intent
import android.os.Debug
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tngblt.playlistr.utils.DataAdaptaterRecycler
import com.tngblt.playlistr.utils.DownloadImageTask
import com.tngblt.playlistr.utils.ImageParams
import com.tngblt.playlistr.R
import com.tngblt.playlistr.interfaces.UserService
import com.tngblt.playlistr.models.UserPlaylist
import com.tngblt.playlistr.models.spotifyData.user.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

    private var disposable:Disposable? = null
    private val userService by lazy {
        UserService.create()
    }
    private var currentUser: User? = null
        set(user) {
        field = user
        textViewResult?.text = user?.displayName

        DownloadImageTask(userImage)
            .execute(ImageParams(user?.images!![0].url, null, "userAvatar"))
    }

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

        /**
         * Call the Spotify application to open the authentication window to get the authorization token
         */
        AuthenticationClient.openLoginActivity(this, requestCode, request)

        textViewResult = findViewById(R.id.text_view_result)
        userImage = findViewById(R.id.userImage)

        // Create a new user FIRESTORE
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

    private fun getCurrentUser(apiKey:String) {
        disposable = userService.getCurrentUserData("Bearer $apiKey")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> currentUser = result},
                { error -> Toast.makeText(this, error.message,Toast.LENGTH_LONG).show()}
            )
    }

    private fun getCurrentUserPlaylists(apiKey:String) {

    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    /**
     * function called when the authentication screen has been closed
     * by the user. This is where the Authorization token is retrieved
     */
    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (reqCode == requestCode) {
            val response = AuthenticationClient.getResponse(resultCode, intent)

            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    getCurrentUser(response.accessToken)
                    //UserRequest(OkHttpClient(), response.accessToken)
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
