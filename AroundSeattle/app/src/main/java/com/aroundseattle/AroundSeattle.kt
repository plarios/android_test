package com.aroundseattle

import android.app.Activity
import android.app.Application
import com.aroundseattle.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class AroundSeattle : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().application(this)
                .build().inject(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}
