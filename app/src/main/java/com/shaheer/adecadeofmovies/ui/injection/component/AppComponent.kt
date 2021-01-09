package com.shaheer.adecadeofmovies.ui.injection.component

import com.shaheer.adecadeofmovies.ui.App
import com.shaheer.adecadeofmovies.ui.injection.module.DataModule
import com.shaheer.adecadeofmovies.ui.injection.module.PresentationModule
import com.shaheer.adecadeofmovies.ui.injection.module.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        UiModule::class,
//        DomainModule::class,
        DataModule::class,
        PresentationModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance app: App): AppComponent
    }

    fun inject(app: App)
}