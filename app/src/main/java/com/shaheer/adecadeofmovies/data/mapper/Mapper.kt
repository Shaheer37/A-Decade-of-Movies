package com.shaheer.adecadeofmovies.data.mapper

interface Mapper<L, R> {
    fun mapToLocal(remote: R): L
    fun mapToRemote(local: L): R
}