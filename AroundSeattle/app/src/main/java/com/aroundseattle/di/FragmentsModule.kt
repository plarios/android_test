package com.aroundseattle.di

import com.aroundseattle.ui.MapFragment
import com.aroundseattle.ui.SearchFragment
import com.aroundseattle.ui.VenueFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun contributeVenueFragment(): VenueFragment
}
