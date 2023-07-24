package structuredconcurency

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

suspend fun main() {
    val vocabulary = Vocabulary()

    findWordsSlowly(vocabulary, "сильный", "любимый") // Working time: 7025
//    findWordsAsync(vocabulary, "сильный", "любимый") // Working time: 5047
//    findWordsWithContextUsing(vocabulary, "сильный", "любимый") // Working time: 5047
}

/*
    Start searching сильный
    Done searching сильный
    Start searching любимый
    Done searching любимый
    Make some other stuff
    Found сильный && любимый
    Working time: 7025
    End some other stuff
 */
// Loads sequentially
suspend fun findWordsSlowly(vocabulary: Vocabulary, word: String, word2: String) {
    val result: String
    val result2: String

    // эта часть будет работать последовательно
    val time = measureTimeMillis {
        result = vocabulary.find(word, withTime = 2000)
        result2 = vocabulary.find(word2, withTime = 5000)
    }

    println("Make some other stuff")
    println("Found $result && $result2")
    println("Working time: $time")
    println("End some other stuff")
}

/*
    Make some other stuff
    Start searching сильный
    Start searching любимый
    Done searching сильный
    Done searching любимый
    Found сильный && любимый
    Working time: 5047
    End some other stuff
 */
suspend fun findWordsAsync(vocabulary: Vocabulary, word: String, word2: String) = coroutineScope {
    val start = System.currentTimeMillis()
    val deferred = async { vocabulary.find(word, withTime = 2000) } // создали корутину, но не запустили её
    val deferred2 = async { vocabulary.find(word2, withTime = 5000) } // создали корутину, но не запустили её

    println("Make some other stuff")

    // при вызовах await() запускается корутина и ожидается её значение.
    // на delay() выполнение первое корутины приостанавливается и поток берёт вторая
    // т.е.: 1 начинает работать, 1 приостанавливается,
    // 2 начинает работать, 2 приостанавливается, 1 завершает свои вычисления, 2 завершает свои вычисления,
    // печатается результат println
    println("Found ${deferred.await()} && ${deferred2.await()}")
    println("Working time: ${System.currentTimeMillis() - start}")
    println("End some other stuff")
}

/*
    Start searching сильный
    Start searching любимый
    Done searching сильный
    Done searching любимый
    Make some other stuff
    Found сильный && любимый
    Working time: 5047
    End some other stuff
 */
suspend fun findWordsWithContextUsing(vocabulary: Vocabulary, word: String, word2: String) = coroutineScope {
    val start = System.currentTimeMillis()
    val deferred: Deferred<String>
    val deferred2: Deferred<String>

    withContext(CoroutineName("test-name")) { // создали новую корутину
        deferred = async { vocabulary.find(word, withTime = 2000) } // создали и запустили новую корутину
        deferred2 = async { vocabulary.find(word2, withTime = 5000) } // создали и запустили новую корутину
    } // Ожидается завершение созданных внутри корутин, после этого только продолжится выполнение внешней

    println("Make some other stuff")
    println("Found ${deferred.await()} && ${deferred2.await()}")
    println("Working time: ${System.currentTimeMillis() - start}")
    println("End some other stuff")
}

suspend fun findWordsAsyncWithCatch(vocabulary: Vocabulary, word: String, word2: String) = coroutineScope {
    val start = System.currentTimeMillis()
    val deferred = async { vocabulary.find(word, withTime = 2000) }
    val deferred2 = async { vocabulary.find(word2, withTime = 5000) }

    println("Make some other stuff")

    runCatching {
        println("Found ${deferred.await()} && ${deferred2.await()}")
    }
        .also { println("Working time: ${System.currentTimeMillis() - start}") }
        .onFailure {
            println("Deffered still running? ${deferred.isActive}")
            println("Deffered is canceled? ${deferred.isCancelled}")
            println("Deffered2 still running? ${deferred2.isActive}")
            println("Deffered2 is canceled? ${deferred2.isCancelled}")
        }.getOrThrow()

    println("End some other stuff")
}
