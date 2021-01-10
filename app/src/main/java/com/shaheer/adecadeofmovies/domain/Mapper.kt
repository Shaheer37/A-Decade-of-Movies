package com.shaheer.adecadeofmovies.domain

interface Mapper<L, R> {
    fun mapToLocal(remote: R): L
    fun mapToRemote(local: L): R
}