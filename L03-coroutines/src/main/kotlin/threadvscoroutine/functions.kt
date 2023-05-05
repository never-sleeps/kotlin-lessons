package threadvscoroutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun main() {
    val threadCount = 100_000
    val millis = 2000L

    // Failed to start the native thread for java.lang.Thread "Thread-4073"
    // Exception in thread "main" java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
    runThread(threadCount, millis)

    // without errors
//    runBlocking {
//        runCoroutine(threadCount, millis)
//    }
}

fun runThread(threadCount: Int, millis: Long) {
    val threads = List(threadCount) {
        thread {
            Thread.sleep(millis)
            print(".")
        }
    }

    threads.forEach { it.join() }
}

suspend fun runCoroutine(threadCount: Int, millis: Long) = coroutineScope {
    List(threadCount) {
        launch {
            delay(millis)
            print(".")
        }
    }
}
