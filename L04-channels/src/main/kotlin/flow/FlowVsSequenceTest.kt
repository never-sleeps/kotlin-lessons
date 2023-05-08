package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class FlowVsSequenceTest {
    /*
        1
        2
        3
        4
        5
        I'm not blocked 1
        I'm not blocked 2
        I'm not blocked 3
        I'm not blocked 4
        I'm not blocked 5
     */
    @Test
    fun sequenceTest(): Unit = runBlocking {
        launch {
            for (k in 1..5) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        sequence {
            for (i in 1..5) {
                Thread.sleep(1000)
                yield(i)
            }
        }.forEach { println(it) }
    }

    /*
        I'm not blocked 1
        1
        I'm not blocked 2
        2
        I'm not blocked 3
        3
        I'm not blocked 4
        4
        I'm not blocked 5
        5
        Flow end
     */
    @Test
    fun flowTest(): Unit = runBlocking {
        launch {
            for (k in 1..5) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        flow {
            for (i in 1..5) {
                delay(1000)
                emit(i)
            }
        }.collect { println(it) }

        println("Flow end")
    }
}