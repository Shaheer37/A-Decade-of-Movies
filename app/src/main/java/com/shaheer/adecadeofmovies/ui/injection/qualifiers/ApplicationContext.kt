package com.shaheer.adecadeofmovies.ui.injection.qualifiers

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext(val value: String = "Application Context")
