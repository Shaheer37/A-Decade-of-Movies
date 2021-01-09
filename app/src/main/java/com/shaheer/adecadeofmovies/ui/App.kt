package com.shaheer.adecadeofmovies.ui

import android.app.Application
import com.shaheer.adecadeofmovies.ui.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class App: Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    open fun initDagger(){
        DaggerAppComponent.factory().create(this).inject(this)
    }

    override fun onCreate() {
        super.onCreate()

        initDagger()
    }
}