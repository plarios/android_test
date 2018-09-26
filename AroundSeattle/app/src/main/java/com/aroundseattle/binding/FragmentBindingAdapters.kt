package com.aroundseattle.binding

import android.databinding.BindingAdapter
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.aroundseattle.data.foursquare.Venue
import com.bumptech.glide.Glide
import javax.inject.Inject
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.util.Log
import com.aroundseattle.data.FavoriteVenue
import com.aroundseattle.R
import com.aroundseattle.util.UrlSigner
import kotlinx.android.synthetic.main.fragment_venue.view.*


/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    var apiKey: String? = null
    var urlSigner: UrlSigner? = null

    private fun initKeys() {
        val context = fragment.context
        val ai = context!!.getPackageManager().getApplicationInfo(context.getPackageName(),
                PackageManager.GET_META_DATA)
        apiKey = ai.metaData.get("com.google.android.geo.API_KEY") as String
        urlSigner = UrlSigner(ai.metaData.get("com.google.android.geo.SECRET") as String)
    }

    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter("staticMap")
    fun bindStaticMap(imageView: ImageView, venue: Venue?)  {
        if (venue != null) {
            if (apiKey == null) {
                initKeys()
            }
            val path = "/maps/api/staticmap"
            val query = "center=Seattle,WA&zoom=14&size=640x640&maptype=roadmap" +
                    "&markers=color:green%7C47.6062,-122.3321" +
                    "&markers=color:red%7C" + venue.location.lat + "," + venue.location.lng +
                    "&key="+apiKey
            val url = "https://maps.googleapis.com"  + urlSigner?.signRequest(path, query)
            Glide.with(fragment).load(url).into(imageView)
        } else {
            imageView.setImageBitmap(null)
        }
    }

    @BindingAdapter("favoriteIcon")
    fun bindFavoriteIcon(imageView: ImageView, favoriteVenue: FavoriteVenue?) {
        var res: Int = R.drawable.favorite_border
        if (favoriteVenue != null &&  favoriteVenue.favorite)
            res = R.drawable.favorite
        imageView.setImageDrawable(fragment.resources.getDrawable(res))
    }
}
