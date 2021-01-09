package com.shaheer.adecadeofmovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject lateinit var viewModel: MainViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MainViewModel::class.java)
    }
}