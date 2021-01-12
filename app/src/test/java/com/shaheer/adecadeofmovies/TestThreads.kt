package com.shaheer.adecadeofmovies

import com.shaheer.adecadeofmovies.domain.executor.ExecutionThreads
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestThreads(): ExecutionThreads {
    override val uiScheduler: Scheduler
        get() = Schedulers.trampoline()

    override val ioScheduler: Scheduler
        get() = Schedulers.trampoline()
}