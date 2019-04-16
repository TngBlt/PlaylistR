package com.tngblt.playlistr.activities

import android.app.Activity
import android.content.Context
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
import android.inputmethodservice.Keyboard
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.tngblt.playlistr.utils.DataAdaptaterRecycler
import com.tngblt.playlistr.utils.DownloadImageTask
import com.tngblt.playlistr.utils.ImageParams
import com.tngblt.playlistr.R
import com.tngblt.playlistr.interfaces.ApiService
import com.tngblt.playlistr.models.spotifyData.playlist.Playlist
import com.tngblt.playlistr.models.spotifyData.user.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private val requestCode = 1997
    private val clientId = "148896cafca44560a3f8f3c7031a50d0"
    private val redirectUri = "https://com.tngblt.playlistr/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    var db = FirebaseFirestore.getInstance()

    private var textViewResult:TextView? = null
    private var userImage:ImageView? = null
    private var apiKey: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DataAdaptaterRecycler
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var currentUserPlaylist: List<Playlist>? = null
        set(value) {
            field = value
            viewAdapter.setData(value!!)
        }
    private var disposable:Disposable? = null
    private val apiService by lazy {
        ApiService.create()
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
        viewAdapter = DataAdaptaterRecycler()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(false)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    override fun onStart() {


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
        super.onStart()

        textViewResult = findViewById(R.id.text_view_result)

        textViewResult?.setOnClickListener { v -> openUserActivity(v) }

        userImage = findViewById(R.id.userImage)

        findViewById<EditText>(R.id.search_field).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    //Log.d("MainActivity", v.text.toString())
                    if (apiKey != null) {
                        getResearchResult("Bearer $apiKey", v.text.toString())
                    }
                    true
                }
                else ->  {
                    Log.wtf("MainActivity", actionId.toString())
                    Log.wtf("MainActivity", "Erreur de recherche... ?")
                    false
                }
            }
        }

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
        disposable = apiService.getCurrentUserData("Bearer $apiKey")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> currentUser = result},
                { error -> Toast.makeText(this, error.message,Toast.LENGTH_LONG).show()}
            )
    }

    private fun getCurrentUserPlaylists(apiKey:String) {
        disposable = apiService.getCurrentUserPlaylists("Bearer $apiKey")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> currentUserPlaylist = result.playlists},
                { error -> Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()}
            )
    }

    private fun getUser(apiKey: String, user_id: String) {
        disposable = apiService.getUserData(user_id,"Bearer $apiKey")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> },// TODO: Add to friends list
                { error -> Toast.makeText(this, error.message,Toast.LENGTH_LONG).show()}
            )
    }

    private fun getUserPlaylists(apiKey: String, user_id: String) {
        disposable = apiService.getUserPlaylists(user_id,"Bearer $apiKey")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> },
                { error -> Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()}
            )
    }

    private fun getResearchResult(apiKey: String, q: String) {
        disposable = apiService.getResearchPlaylistResult(q,authHeader = apiKey) // can filter research here by adding the type parameter
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result ->
                    hideKeyboard(this)
                    currentUserPlaylist = result.playlists!!.items
                    Toast.makeText(this,result.toString(), Toast.LENGTH_LONG).show()
                    Log.wtf("MainActivity/Search", result.toString()) // TODO: load result in list below
                },
                {error ->
                    hideKeyboard(this)
                    Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                    Log.wtf("MainActivity/Search", error.message)
                }
            )
    }

    private fun hideKeyboard(activity: Activity) {
        val view: View = activity.findViewById(android.R.id.content)
        val imm:InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
                    apiKey = response.accessToken
                    getCurrentUser(response.accessToken)
                    getCurrentUserPlaylists(response.accessToken)
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                    Log.wtf("MainActivity", "Error : " + response.error)
                }
                else -> {
                    Log.wtf("MainActivity", "Error : The auth flow must have been cancelled")
                }
            }
        }
    }

    fun openUserActivity(view: View) {
        Toast.makeText(this,"Opening user profile...", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, UserProfileActivity::class.java)
        // To pass any data to next activity
        //intent.putExtra("keyIdentifier", "")
        // start your next activity
        startActivity(intent)
    }

}
