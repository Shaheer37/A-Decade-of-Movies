package com.shaheer.adecadeofmovies.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
}