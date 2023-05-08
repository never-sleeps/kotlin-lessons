package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

class FlowOperatorsTest {

    /*
        1
        Result number 2
        2
        3
        Result number 4
        4
     */
    @Test
    fun test1(): Unit = runBlocking {
        flowOf(1, 2, 3, 4)
            .onEach { println(it) }
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .collect { println("Result number $it") }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private val ApiDispatcher = newSingleThreadContext("Api-Thread")

    @OptIn(DelicateCoroutinesApi::class)
    private val DbDispatcher = newSingleThreadContext("Db-Thread")

    fun <T> Flow<T>.printThreadName(msg: String) =
        this.onEach { println("Msg = $msg, thread name = ${Thread.currentThread().name}") }

    /*
        Msg = api call, thread name = Api-Thread @coroutine#3
        Msg = db call, thread name = Db-Thread @coroutine#2
        Msg = last operation, thread name = Test worker @coroutine#1
        On each 11
        Msg = api call, thread name = Api-Thread @coroutine#3
        Msg = db call, thread name = Db-Thread @coroutine#2
        Msg = last operation, thread name = Test worker @coroutine#1
        On each 21
        Msg = api call, thread name = Api-Thread @coroutine#3
        Msg = db call, thread name = Db-Thread @coroutine#2
        Msg = last operation, thread name = Test worker @coroutine#1
        On each 31
     */
    @Test
    fun `test with flowOn`(): Unit = runBlocking {
        flowOf(10, 20, 30)
            .filter { it % 2 == 0 }
            .map {
                delay(2000)
                it
            }
            .printThreadName("api call")
            .flowOn(ApiDispatcher) // Предыдущие строки будут выполнены в ApiDispatcher
            .map { it + 1 }
            .printThreadName("db call")
            .flowOn(DbDispatcher) // Предыдущие строки будут выполнены в DbDispatcher (т.е. map и printThreadName)
            .printThreadName("last operation")
            .onEach { println("On each $it") }
            .collect() // Предыдущие строки будут выполнены в default dispatcher'е
    }

    /*
        On start
        On each: 1
        On each: 2
        On each: 3
         On completion
        Catch: Custom error!
     */
    @Test
    fun `test with Exception catching`(): Unit = runBlocking {
        flow { // будут сгенерены 3 элемента с задержкой в 1 сек и в финале будет выкинут exception
            while (true) {
                emit(1)
                delay(1000)
                emit(2)
                delay(1000)
                emit(3)
                delay(1000)
                throw RuntimeException("Custom error!")
            }
        }
            .onStart { println("On start") }
            .onCompletion { println(" On completion") }
            .catch { println("Catch: ${it.message}") }
            .onEach { println("On each: $it") }
            .collect { } // результат не интересен, интересен процесс, поэтому пустой
    }

    @Test
    fun `test with buffer`(): Unit = runBlocking {
        var sleepIndex = 1
        flow { // будут сгенерены 3 элемента с задержкой 500 милисек
            while (sleepIndex < 3) {
                delay(500)
                emit(sleepIndex)
            }
        }
            .onEach { println("Send to flow: $it") }
            .buffer(3, BufferOverflow.DROP_LATEST)
            .onEach { println("Processing : $it") }
            .collect { // обработка элементов с задержкой в 2 сек. то есть обработка медленная, а генереация быстрая
                println("Sleep")
                sleepIndex++
                delay(2_000)
            }
    }

    /*
        (1, 2)
        (2, 3)
        (3, 4)
     */
    @Test
    fun `test with my own operator`(): Unit = runBlocking {
        flowOf(1, 2, 3, 4)
            .zipWithNext()
            .collect { println(it) }
    }

    @Test
    fun test6(): Unit = runBlocking {
        val coldFlow = flowOf(100, 101, 102, 103, 104, 105).onEach { println("Cold: $it") }

        launch { coldFlow.collect() }
        launch { coldFlow.collect() }

        val hotFlow = flowOf(200, 201, 202, 203, 204, 205)
            .onEach { println("Hot: $it") }
            .shareIn(this, SharingStarted.Lazily)

        launch { hotFlow.collect() }
        launch { hotFlow.collect() }

        delay(500)
        coroutineContext.cancelChildren()
    }

    @Test
    fun test7(): Unit = runBlocking {
        val list = flow {
            emit(1)
            delay(100)
            emit(2)
            delay(100)
        }
            .onEach { println("$it") }
            .toList()

        println("List: $list")
    }

    @Test
    fun test8(): Unit = runBlocking {
        val list = flow {
            var index = 0
            // If there is an infinite loop here, while (true)
            // then we will never output to the console
            //  println("List: $list")
            while (index < 10) {
                emit(index++)
                delay(100)
            }
        }
            .onEach { println("$it") }
            .toList()

        println("List: $list")
    }
}