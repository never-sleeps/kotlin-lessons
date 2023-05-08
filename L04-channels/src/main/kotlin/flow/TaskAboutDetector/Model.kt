package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.util.*
import kotlin.concurrent.schedule

/**
 * Измерение
 */
data class Sample(
    val serialNumber: String,
    val value: Double,
    val timestamp: Instant = Instant.now()
)

/**
 * Датчик
 */
interface Detector {
    fun samples(): Flow<Sample>
}

class CoroutineDetector(
    private val serialNumber: String,
    private val sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long // задержка после генерации каждого значения
) : Detector {
    override fun samples(): Flow<Sample> =
        flow {
            val values = sampleDistribution.iterator()
            while (true) {
                emit(Sample(serialNumber, values.next()))
                delay(samplePeriod)
            }
        }
}

class BlockingDetector(
    private val serialNumber: String,
    private val sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long // задержка после генерации каждого значения
) : Detector {
    override fun samples(): Flow<Sample> =
        flow {
            val values = sampleDistribution.iterator()
            while (true) {
                emit(Sample(serialNumber, values.next()))
                Thread.sleep(samplePeriod) // блокирование обеспечивается вызовом этой функции
            }
        }.flowOn(Dispatchers.IO) // чтобы не блочить поток, в котором работает наша корутина
}

class CallbackDetector(
    private val serialNumber: String,
    private val sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long // задержка после генерации каждого значения
) : Detector {
    override fun samples(): Flow<Sample> =
        callbackFlow {
            val values = sampleDistribution.iterator()

            val timer = Timer()
            timer.schedule(0L, samplePeriod) {
                trySendBlocking(Sample(serialNumber, values.next()))
            }
            timer.schedule(10_000L) { close() } // таймер закроется через 10 сек

            awaitClose { timer.cancel() } // также таймер закроется при отмене флоу
        }
}
