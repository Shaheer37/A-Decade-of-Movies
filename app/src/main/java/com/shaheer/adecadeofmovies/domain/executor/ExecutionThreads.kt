package com.shaheer.adecadeofmovies.domain.executor

import io.reactivex.Scheduler

interface ExecutionThreads {
    val uiScheduler: Scheduler
    val ioScheduler: Scheduler
}