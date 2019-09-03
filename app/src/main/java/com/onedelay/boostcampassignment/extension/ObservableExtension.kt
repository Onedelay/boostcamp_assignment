package com.onedelay.boostcampassignment.extension

import android.os.Looper
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = { throw it }
private val onCompleteStub: () -> Unit = {}

internal fun <T : Any> Observable<T>.subscribeOf(
        onNext: (T) -> Unit          = onNextStub,
        onComplete: () -> Unit       = onCompleteStub,
        onError: (Throwable) -> Unit = onErrorStub
): Disposable = subscribe(onNext, onError, onComplete)

internal fun <T : Any> Observable<T>.subscribe(
        observeOn: Scheduler,
        onNext: (T) -> Unit          = onNextStub,
        onComplete: () -> Unit       = onCompleteStub,
        onError: (Throwable) -> Unit = onErrorStub
): Disposable {
    return observeOn(observeOn).subscribeOf(onNext, onComplete, onError)
}

internal fun <T : Any> Observable<T>.subscribeOnMain(
        onNext: (T) -> Unit          = onNextStub,
        onComplete: () -> Unit       = onCompleteStub,
        onError: (Throwable) -> Unit = onErrorStub
): Disposable {
    return assertMain().subscribe(onNext, onError, onComplete)
}

internal fun <T> Observable<T>.assertMain(): Observable<T> {
    if(Looper.myLooper() != Looper.getMainLooper()) {
        throw RuntimeException("require main thread. but it's not main tread. it's ${Thread.currentThread().name}")
    }

    return this
}

internal fun <T : Any, R : Any> Observable<T>.consumeLatestFrom(consumee: Observable<R>): Observable<Pair<T, R>> {
    return consumee.sample(this).withLatestFrom(this, BiFunction { r, t ->
        Pair(t, r)
    })
}

internal fun <T, U> Observable<T>.withLatestFromFirst(other: ObservableSource<U>): Observable<T>
        = withLatestFrom(other, BiFunction{ t, _ -> t })

internal fun <T, U> Observable<T>.withLatestFromSecond(other: ObservableSource<U>): Observable<U>
        = withLatestFrom(other, BiFunction{ _, u -> u })

internal fun <T, U> Observable<T>.pairWithLatestFrom(other: ObservableSource<U>): Observable<Pair<T, U>>
        = withLatestFrom(other, BiFunction{ t, u -> Pair(t,u)  })

internal fun <T : Any> Observable<T>.applyOf(action: (T) -> Unit): Observable<T> {
    return map {
        action(it)

        it
    }
}