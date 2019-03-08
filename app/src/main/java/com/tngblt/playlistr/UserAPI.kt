package com.tngblt.playlistr

import android.os.AsyncTask
import java.io.IOException
import okhttp3.*


class UserAPI constructor(client: OkHttpClient, accessToken:String, userId:String) : AsyncTask<Any,Any, String>() {
    private val mClient = client
    private val mAccessToken = accessToken
    private val mUserId = userId
    private val apiUrl = "https://api.spotify.com/v1/users/"

    override fun doInBackground(vararg params: Any?): String {
        try {
            val request = Request.Builder()
                .url(apiUrl + mUserId )
                .get()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer $mAccessToken")
                .build()

            mClient.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val res = response.body()

                        // Do something with the response
                    }
                })
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}