package com.tngblt.playlistr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.util.LruCache
import android.view.View
import android.widget.ImageView

class ImageParams(val url:String, val memoryCache:LruCache<String, Bitmap>?, val imgId: String)

class DownloadImageTask(internal var bmImage: ImageView?) : AsyncTask<ImageParams, Void, Bitmap>() {

    override fun doInBackground(vararg params:ImageParams?): Bitmap? {
        val urldisplay = params[0]?.url
        var mIcon11: Bitmap? = null
        try {
            val `in` = java.net.URL(urldisplay).openStream()
            mIcon11 = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }

        if(params[0] != null) {
            // Add to the cache here
            addBitmapToMemoryCache(params[0]?.imgId, mIcon11, params[0]?.memoryCache)
        }
        return mIcon11
    }

    override fun onPostExecute(result: Bitmap) {
        bmImage?.setImageBitmap(result)
        bmImage?.visibility = View.VISIBLE
    }

    private fun addBitmapToMemoryCache(key: String?, bitmap: Bitmap?, memoryCache: LruCache<String, Bitmap>?) {
        if (getBitmapFromMemCache(key, memoryCache) == null) {
            memoryCache?.put(key, bitmap)
        }
    }

    private fun getBitmapFromMemCache(key: String?, memoryCache: LruCache<String, Bitmap>?): Bitmap? {
        return memoryCache?.get(key)
    }
}