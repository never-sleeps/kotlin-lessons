package flow.TaskAboutDetector

import flow.BlockingDetector
import flow.CallbackDetector
import flow.CoroutineDetector
import flow.Detector
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.random.Random

class FlowTest {
    private fun getDetectorList() : List<Detector> {
        val random = Random.Default
        val seq = sequence {
            while (true) {
                yield(random.nextDouble())
            }
        }

        return listOf(
            CoroutineDetector("coroutine", seq, 500L),
            BlockingDetector("blocking", seq, 800L),
            CallbackDetector("callback", seq, 2_000L)
        )
    }

    /*
        Sample(serialNumber=coroutine, value=0.7735872762516007, timestamp=2023-05-08T01:39:35.934331Z)
        Sample(serialNumber=blocking, value=0.2722506578904603, timestamp=2023-05-08T01:39:35.948962Z)
        Sample(serialNumber=callback, value=0.5195713116970618, timestamp=2023-05-08T01:39:36.008275Z)
        Sample(serialNumber=coroutine, value=0.11610265059909208, timestamp=2023-05-08T01:39:36.448140Z)
        Sample(serialNumber=blocking, value=0.8423067882203057, timestamp=2023-05-08T01:39:36.753714Z)
        Sample(serialNumber=coroutine, value=0.848070119946327, timestamp=2023-05-08T01:39:36.950106Z)
        Sample(serialNumber=coroutine, value=0.18286279375776993, timestamp=2023-05-08T01:39:37.451115Z)
        Sample(serialNumber=blocking, value=0.8809312375265219, timestamp=2023-05-08T01:39:37.557612Z)
     */
    @Test
    // сырые данные от датчиков (по нескольку объектов данных от coroutine в секунду и по одному объекту от callback раз в несколько секунд)
    fun test1(): Unit = runBlocking {
        getDetectorList()
            .map { it.samples() }
            .merge() // Merges the given flows into a single flow without preserving an order of elements
            .onEach { println(it) }
            .launchIn(this) // Terminal flow operator that launches the collection of the given flow in the scope.

        delay(2000)
        coroutineContext.cancelChildren()
    }

    /*
        Sample(serialNumber=coroutine, value=0.6939983228331523, timestamp=2023-05-08T01:44:31.226019Z)
        Sample(serialNumber=blocking, value=0.8463918271550901, timestamp=2023-05-08T01:44:31.529888Z)
        Sample(serialNumber=callback, value=0.6725913992092568, timestamp=2023-05-08T01:44:30.725583Z)
        Sample(serialNumber=coroutine, value=0.6508153399585144, timestamp=2023-05-08T01:44:32.247565Z)
        Sample(serialNumber=blocking, value=0.7364142809690762, timestamp=2023-05-08T01:44:32.335284Z)
        Sample(serialNumber=callback, value=0.6725913992092568, timestamp=2023-05-08T01:44:31.735126Z)
        Sample(serialNumber=coroutine, value=0.7020531819099461, timestamp=2023-05-08T01:44:33.254308Z)
        Sample(serialNumber=blocking, value=0.7044273609600591, timestamp=2023-05-08T01:44:33.140386Z)
        Sample(serialNumber=callback, value=0.40472121416629725, timestamp=2023-05-08T01:44:32.731035Z)
        Sample(serialNumber=coroutine, value=0.8954367608452413, timestamp=2023-05-08T01:44:34.262479Z)
        Sample(serialNumber=blocking, value=0.12711883154368342, timestamp=2023-05-08T01:44:33.945468Z)
        Sample(serialNumber=callback, value=0.10521812317896917, timestamp=2023-05-08T01:44:34.731416Z)
     */
    @Test
    // данные от каждого из датчиков раз в секунду (если нового нет, то последнее)
    fun test2(): Unit = runBlocking {
        val desiredPeriod = 1000L
        getDetectorList()
            .map {
                it.samples()
                    // Returns a flow that produces element by transform function every time the original flow emits a value.
                    .transformLatest { sample ->
                        //println("Start transformLatest for ${sample.serialNumber}")
                        emit(sample)
                        while (true) {
                            delay(desiredPeriod)
                            //println("Add old value to flow in transformLatest for = ${sample.serialNumber}")
                            emit(sample.copy(timestamp = Instant.now()))
                        }
                    }
                    .sample(desiredPeriod) // Returns a flow that emits only the latest value emitted by the original flow during the given sampling period.
            } // до этой строки генерим по одному значению раз в 1 сек
            .merge()
            .onEach { println(it) }
            .launchIn(this)

        delay(5_000)
        coroutineContext.cancelChildren()
    }

    /*
        Sample(serialNumber=blocking, value=0.9106414260974631, timestamp=2023-05-08T01:54:51.338473Z)
        Sample(serialNumber=blocking, value=0.9106414260974631, timestamp=2023-05-08T01:54:51.338473Z)
        Sample(serialNumber=coroutine, value=0.9106414260974631, timestamp=2023-05-08T01:54:51.338473Z)
     */
    @Test
    // нужно получить измерение с наибольшим значением (на каждом шаге выбираем из всех значений максимальное и печатаем именно его)
    fun test3(): Unit = runBlocking {
        val desiredPeriod = 1000L
        val samples = getDetectorList()
            .map {
                it.samples()
                    .transformLatest { sample ->
//                    println("Start transformLatest for ${sample.serialNumber}")
                        emit(sample)
                        while (true) {
                            delay(desiredPeriod)
//                        println("Add old value to flow in transformLatest for = ${sample.serialNumber}")
                            emit(sample.copy(timestamp = Instant.now()))
                        }
                    }
                    .sample(desiredPeriod)
            } // до этой строки генерим по одному значению раз в 1 сек
            .merge()
            // Converts a cold Flow into a hot SharedFlow that is started in the given coroutine scope,
            // sharing emissions from a single running instance of the upstream flow with multiple downstream subscribers,
            // and replaying a specified number of replay values to new subscribers.
            .shareIn(this, SharingStarted.Lazily) // Sharing is started when the first subscriber appears and never stops.

        samples
            .runningReduce { max, current -> maxOf(max, current, compareBy { it.value }) } // определяем максимальный элемент
            .sample(desiredPeriod)
            .onEach { println(it) }
            .launchIn(this)

        delay(5_000)
        coroutineContext.cancelChildren()
    }

    @Test
    fun test4(): Unit = runBlocking {
        val desiredPeriod = 1000L
        val flows = getDetectorList()
            .map {
                it.samples()
                    .transformLatest { sample ->
                        emit(sample)
                        while (true) {
                            delay(desiredPeriod)
                            emit(sample.copy(timestamp = Instant.now()))
                        }
                    }
                    .sample(desiredPeriod)
            }

        var index = 0
        val samples = combineTransform(flows) {
            it.forEach { s -> println("$index: value = $s") }
            index++

            emit(it.maxBy { s -> s.value })
        }
            .shareIn(this, SharingStarted.Lazily)

        samples
            .onEach { println(it) }
            .launchIn(this)

        delay(5_000)
        coroutineContext.cancelChildren()
    }
}