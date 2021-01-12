package com.shaheer.adecadeofmovies.ui

import com.shaheer.adecadeofmovies.domain.executor.ExecutionThreads
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UiThreads @Inject constructor(): ExecutionThreads{
    override val uiScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

    override val ioScheduler: Scheduler
        get() = Schedulers.io()
}