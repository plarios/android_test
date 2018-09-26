package com.aroundseattle.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.aroundseattle.data.foursquare.Venue
import com.aroundseattle.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class MapFragment : SupportMapFragment(), OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val markersMap : HashMap<Marker,Venue> = HashMap()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnInfoWindowClickListener {
            val venue = markersMap.get(it)
            if (venue != null) {
                findNavController().navigate(MapFragmentDirections.showVenue(venue.id))
            }
        }
        val searchViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(SearchViewModel::class.java)
        searchViewModel.results.observe(this, Observer { result ->
            addMapMarkers(map, result)
        })
    }

    private fun addMapMarkers(map: GoogleMap, list: List<Venue>?) {
        map.clear()
        markersMap.clear()
        if (list != null) {
            val bounds = LatLngBounds.builder()

            for (venue in list) {
                val pos = LatLng(venue.location.lat, venue.location.lng)
                bounds.include(pos)
                markersMap.put(map.addMarker(MarkerOptions()
                        .position(pos)
                        .title(venue.name)
                        .snippet(venue.location.address)), venue)
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 44))
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng(47.6062,-122.3321), 11.0f))
        }
    }
}