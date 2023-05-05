package coroutinescope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.suspendingCall(ctx: CoroutineContext) =
    launch(ctx) {
        println("Start_delay")
        delay(500)
        println("foo bar")
    }

fun CoroutineScope.blockingCall(ctx: CoroutineContext) =
    launch(ctx) {
        runBlocking {
            println("Taking delay")
            delay(500)
            println("foo bar")
        }
    }

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    println("Main start")
    val ctx = newSingleThreadContext("MyOwnThread")
    runBlocking {
        repeat(5) {
//            blockingCall(ctx)
            suspendingCall(ctx)
        }
    }
    println("Main end")
}