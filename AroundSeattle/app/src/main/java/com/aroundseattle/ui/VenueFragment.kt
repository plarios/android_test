package com.aroundseattle.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroundseattle.AppExecutors
import com.aroundseattle.R
import com.aroundseattle.binding.FragmentDataBindingComponent
import com.aroundseattle.data.foursquare.Venue
import com.aroundseattle.databinding.FragmentVenueBinding
import com.aroundseattle.viewmodel.VenueViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class VenueFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding: FragmentVenueBinding? = null
    lateinit var venueViewModel: VenueViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_venue,
                container,
                false,
                dataBindingComponent
        )
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)

        venueViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VenueViewModel::class.java)
        venueViewModel.venue.observe(this, Observer { result ->
            binding?.venue = result
            binding?.url?.setOnClickListener {
                openUrl(result)
            }
            binding?.favorite?.setOnClickListener {
                venueViewModel.toggleFavorite(result)
            }
        })
        venueViewModel.favoriteStatus.observe(this, Observer { result ->
            binding?.favoriteStatus = result
        })

        val params = VenueFragmentArgs.fromBundle(arguments)
        venueViewModel.setVenueId(params.venueId)
    }

    fun openUrl(venue: Venue?) {
        if (venue != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(venue.url))
            startActivity(intent)
        }
    }
}
