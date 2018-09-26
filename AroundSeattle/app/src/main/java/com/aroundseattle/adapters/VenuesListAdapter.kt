package com.aroundseattle.adapters

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aroundseattle.AppExecutors
import com.aroundseattle.R
import com.aroundseattle.data.foursquare.Venue
import com.aroundseattle.databinding.CardVenueBinding
import com.aroundseattle.db.FavoritesDao
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil


/**
 * recycler view adapter for venues
 */
class VenuesListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val favoritesDao: FavoritesDao,
        private val clickCallback: ((Venue) -> Unit)?
) : DataBoundListAdapter<Venue, CardVenueBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Venue>() {
            override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
                return oldItem.name == newItem.name
            }
        }
) {

    override fun createBinding(parent: ViewGroup): CardVenueBinding {
        val binding = DataBindingUtil.inflate<CardVenueBinding>(
                LayoutInflater.from(parent.context),
                R.layout.card_venue,
                parent,
                false,
                dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.venue?.let {
                clickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: CardVenueBinding, item: Venue) {
        binding.venue = item
        binding.distance = String.format("%.2f mi", SphericalUtil.computeDistanceBetween(
                LatLng(47.6062,-122.3321),
                LatLng(item.location.lat, item.location.lng)) * 0.000621)
        favoritesDao.findById(item.id).observeForever(){
            if (it != null)
                binding.favorite = it.favorite
            else
                binding.favorite = false
        }
    }
}
