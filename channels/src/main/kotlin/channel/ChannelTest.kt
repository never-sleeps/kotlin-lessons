package channel

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ChannelTest {

    /*
        1
        4
        9
        16
        25
        Done!
     */
    @Test
    fun `simple channel test`(): Unit = runBlocking {
        val channel = Channel<Int>() // создаём канал

        launch { // создаём корутину и пишем в канал 5 элементов
            for (x in 1..5) channel.send(x * x)
            channel.close() // закрываем канал
        }

        for (value in channel) { // читаем из канала (из верхней корутины уже вышли)
            println(value)
        }
        println("Done!")
    }

    /*
        1
        4
        100
        9
        16
        121
        25
        144
        169
        196
        225
     */
    @Test
    fun `test with several coroutines`(): Unit = runBlocking {
        val channel = Channel<Int>()

        launch { // корутина 1 пишет в канал числа меньше 100
            for (x in 1..5) channel.send(x * x)
        }
        launch { // корутина 2 пишет в канал числа больше 100
            for (x in 10..15) channel.send(x * x)
        }
        launch { // // корутина 3 закрывает канал
            delay(2000)
            channel.close()
        }

        for (value in channel) { // исходная корутина печатает значения из канала
            println(value)
        }
    }

    /*
        Consumer 1: 1
        Consumer 1: 100
        Consumer 1: 4
        Consumer 1: 16
        Consumer 1: 121
        Consumer 2: 9
        Consumer 1: 25
        Consumer 1: 169
        Consumer 2: 144
        Consumer 1: 196
        Consumer 2: 225
     */
    @Test
    fun `test with several consumers`(): Unit = runBlocking {
        val channel = Channel<Int>()

        launch {
            for (x in 1..5) channel.send(x * x)
        }
        launch {
            for (x in 10..15) channel.send(x * x)
        }
        launch {
            delay(2000)
            channel.close()
        }

        launch { // consumer 1 читает из канала
            for (value in channel) {
                println("Consumer 1: $value")
            }
        }
        launch { // consumer 2 читает из канала
            for (value in channel) {
                println("Consumer 2: $value")
            }
        }
    }

    /*
        Send value: 1
        Send value: 4
        Send value: 9
        Consumer: 1
        Send value: 16
        Consumer: 4
        Send value: 25
        Consumer: 9
        Send value: 36
        Consumer: 16
        Send value: 49
        Consumer: 25
        Consumer: 36
        Consumer: 49
     */
    @Test
    // поскольку используется capacity = 3 и onBufferOverflow = SUSPEND, первые 3 элемента будут сгенерированы быстро,
    // последующие уже будут генерироваться по мере чтения предыдущих из канала
    fun `test with custom channel`(): Unit = runBlocking {
        val channel = Channel<Int>(
            capacity = 3,
            onBufferOverflow = BufferOverflow.SUSPEND // Suspend on buffer overflow.
        ) {
            // never call, because onBufferOverflow = SUSPEND
            println("Call for value: $it")
        }

        launch { // корутина 1 пишет 10 элементов в канал
            for (x in 1..7) {
                val value = x * x
                channel.send(value)
                println("Send value: $value")
            }
        }
        launch { // корутина 2 закрывает канал
            delay(11000)
            channel.close()
        }

        launch { // корутина 3 читает из канала
            for (value in channel) {
                println("Consumer: $value")
                delay(1000)
            }
        }
    }

}