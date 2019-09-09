package com.onedelay.boostcampassignment

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun something() {
        val strings = Observable.just("a", "b", "c", "d")

        Observable.merge(
                strings.take(1),
                Observables.zip(
                        strings,
                        strings.skip(1)
                )
                        .map { "${it.first} ${it.second}" }
        )
                .subscribe {
                    println(it)
                }

        Thread.sleep(1000)
    }

}