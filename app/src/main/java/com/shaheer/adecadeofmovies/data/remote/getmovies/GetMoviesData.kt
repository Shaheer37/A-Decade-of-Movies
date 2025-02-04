package com.shaheer.adecadeofmovies.data.remote.getmovies

import android.content.Context
import com.google.gson.Gson
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.RemoteMovie
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.Movies
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.ApplicationContext
import io.reactivex.Single
import java.io.IOException
import javax.inject.Inject

interface GetMoviesData {
    operator fun invoke(): Single<List<RemoteMovie>>
}

class GetMoviesDataImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : GetMoviesData {
    override operator fun invoke(): Single<List<RemoteMovie>>{
        return Single.create{ emitter ->
            try {
                val inStream = context.assets.open("movies.json")
                val stringBuilder = StringBuilder()
                var i: Int = 0
                val b = ByteArray(4096)
                while (inStream.read(b).also { i = it } != -1) {
                    stringBuilder.append(String(b, 0, i))
                }
                val movies = gson.fromJson(stringBuilder.toString(), Movies::class.java)
                emitter.onSuccess(movies.movies)
            } catch (e: IOException) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
}