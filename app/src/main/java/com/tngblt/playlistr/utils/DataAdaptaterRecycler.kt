package com.tngblt.playlistr.utils

import android.graphics.Bitmap
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.drawable.BitmapDrawable
import com.tngblt.playlistr.R
import com.tngblt.playlistr.models.spotifyData.playlist.Playlist

/**
 * Class to create the list view (RecyclerView) of all user Playlists
 */
class DataAdaptaterRecycler : RecyclerView.Adapter<DataAdaptaterRecycler.DataViewHolder> {

    private var dataset: List<Playlist>? = null

    constructor() : super() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

    internal fun setData(playlists:List<Playlist>) {
        this.dataset = playlists
        notifyDataSetChanged()
    }

    private lateinit var memoryCache: LruCache<String, Bitmap>

    // https://developer.android.com/guide/topics/ui/layout/recyclerview#kotlin
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class DataViewHolder(val playlistCard: ConstraintLayout) : RecyclerView.ViewHolder(playlistCard)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataViewHolder {
        // create a new view
        val playlistCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_playlist_list, parent, false) as ConstraintLayout
        //playlistImage = playlistCard.findViewById(R.id.playlistImage)
        // set the view's size, margins, paddings and layout parameters

        return DataViewHolder(playlistCard)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.playlistCard.findViewWithTag<TextView>("playlist_title").text = dataset?.get(position)?.name
        //holder.playlistCard.findViewWithTag<TextView>("playlist_status").text = if (dataset?.get(position)?.public!!) "public" else "private"
        val img:ImageView = holder.playlistCard.findViewWithTag<ImageView>("playlistImage")
        img.visibility = View.INVISIBLE
        if(dataset?.get(position)?.images!![0].url?.isNotEmpty()!!){
            val imgId = if (img.drawable !=null) (img.drawable as BitmapDrawable).bitmap.generationId else 0
            loadBitmap(dataset?.get(position)?.name!! ,img, dataset?.get(position)?.images!![0].url!!)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = if (!dataset.isNullOrEmpty() ) dataset!!.size else 0

    fun loadBitmap(resId: String, imageView: ImageView, img_url : String) {

        val bitmap: Bitmap? = getBitmapFromMemCache(resId)?.also {
            imageView.setImageBitmap(it)
            imageView.visibility = View.VISIBLE
        } ?: run {
            //load image
            DownloadImageTask(imageView)
                .execute(ImageParams(img_url, memoryCache, resId))
            null
        }
    }

    fun getBitmapFromMemCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }
}

