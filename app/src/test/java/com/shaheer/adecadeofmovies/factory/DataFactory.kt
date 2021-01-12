package com.shaheer.adecadeofmovies.factory

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object DataFactory {

    public const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun getLocale() = Locale.US;

    fun randomString():String{
        return UUID.randomUUID().toString()
    }

    fun randomRating(): Int {
        return ThreadLocalRandom.current().nextInt(1, 5 + 1)
    }

    fun randomDate(): String{
        return SimpleDateFormat(dateFormat).format(Date())
    }

    fun randomYear(): Int {
        return ThreadLocalRandom.current().nextInt(2009, 2018 + 1)
    }

    fun randomMonth(): Int {
        return ThreadLocalRandom.current().nextInt(0, 12 + 1)
    }

    fun randomDay(): Int {
        return ThreadLocalRandom.current().nextInt(0, 30 + 1)
    }
}