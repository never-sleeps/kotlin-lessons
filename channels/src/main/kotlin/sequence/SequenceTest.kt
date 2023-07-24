import org.junit.jupiter.api.Test
import sequence.Color
import sequence.Figure
import sequence.Shape

fun processAsList(
    words: List<String>,
    wordLength: Int,
    take: Int,
    printOperation: Boolean,
) {
    println("Processing list")
    var counter = 0

    val lengthsList = words
        .filter {
            counter++
            if (printOperation) println("filter: $it")
            it.length > wordLength
        }
        .map {
            counter++
            if (printOperation) println("length: ${it.length}")
            it.length
        }
        .take(take)

    println("Lengths of first $take words longer than $wordLength chars:")
    println(lengthsList)

    println("List counter: $counter")
    println()
}

fun processAsSeq(
    words: List<String>,
    wordLength: Int,
    take: Int,
    printOperation: Boolean,
) {
    println("Processing sequence")
    var counter = 0

    // convert the List to a Sequence
    val wordsSequence = words.asSequence()

    val lengthsSequence = wordsSequence
        .filter {
            counter++
            if (printOperation) println("filter: $it")
            it.length > wordLength
        }
        .map {
            counter++
            if (printOperation) println("length: ${it.length}")
            it.length
        }
        .take(take)
        .toList()

    println("Lengths of first $take words longer than $wordLength chars")
    println(lengthsSequence)

    println("Sequence counter: $counter")
    println()
}

class SequenceTest {
    companion object {
        private val testFigure = listOf(
            Figure(Color.GREEN, Shape.CIRCLE),
            Figure(Color.VIOLET, Shape.SQUARE),
            Figure(Color.BLUE, Shape.RHOMBUS),
            Figure(Color.RED, Shape.TRIANGLE),
        )
    }

    /*
        Change color
        Change color
        Change color
        Change color
        Filter by shape
        Filter by shape
        Figure: Figure(color=YELLOW, shape=SQUARE)
        Counter: 6
     */
    @Test
    fun `test collection`() {
        var counter = 0
        val figure = testFigure
            .map {
                counter++
                println("Change color")
                it.copy(color = Color.YELLOW)
            }
            .first {
                counter++
                println("Filter by shape")
                it.shape == Shape.SQUARE
            }
        println("Figure: $figure")
        println("Counter: $counter")
    }

    /*
        Change color
        Filter by shape
        Change color
        Filter by shape
        Figure: Figure(color=YELLOW, shape=SQUARE)
        Counter: 4
     */
    @Test
    fun `test sequence`() {
        var counter = 0
        val figure = testFigure.asSequence()
            .map {
                counter++
                println("Change color")
                it.copy(color = Color.YELLOW)
            }
            .first {
                counter++
                println("Filter by shape")
                it.shape == Shape.SQUARE
            }
        println("Figure: $figure")
        println("Counter: $counter")
    }

    /*
        Processing list
        Lengths of first 3 words longer than 3 chars:
        [5, 5, 5]
        List counter: 14

        Processing sequence
        Lengths of first 3 words longer than 3 chars
        [5, 5, 5]
        Sequence counter: 8
     */
    @Test
    fun smallSequence() {
        val words = "The quick brown fox jumps over the lazy dog".split(" ")

        val wordLength = 3
        val take = 3
        val printOperation = false

        processAsList(words, wordLength, take, printOperation)
        processAsSeq(words, wordLength, take, printOperation)
    }

    /*
        Processing list
        Lengths of first 10 words longer than 3 chars:
        [9, 5, 8, 7, 12, 6, 9, 9, 11, 5]
        List counter: 99

        Processing sequence
        Lengths of first 10 words longer than 3 chars
        [9, 5, 8, 7, 12, 6, 9, 9, 11, 5]
        Sequence counter: 28
     */
    @Test
    fun bigSequence() {
        val words = (
            "So, the sequences let you avoid building results of intermediate steps, " +
                "therefore improving the performance of the whole collection processing chain. " +
                "However, the lazy nature of sequences adds some overhead which may be significant " +
                "when processing smaller collections or doing simpler computations. Hence, you should consider both " +
                "Sequence and Iterable and decide which one is better for your case."
            )
            .split(" ")

        val wordLength = 3
        val take = 10

        val printOperation = false

        processAsList(words, wordLength, take, printOperation)
        processAsSeq(words, wordLength, take, printOperation)
    }

    @Test
    fun collectionIsNotLazy() {
        var counter = 0
        val list = listOf(1, 2, 3, 4)
            .map {
                counter++
                it * it
            }
            .take(2)
        println("List: $list")
        println("Counter: $counter")
    }

    @Test
    fun sequenceIsLazy() {
        var counter = 0
        val sequence = sequenceOf(1, 2, 3, 4)
            .map {
                counter++
                it * it
            }
            .take(2)

        println("Sequence: $sequence")
        println("Counter: $counter")

        val result = sequence
            .toList()

        println("Result: $result")
        println("Counter: $counter")
    }

    @Test
    fun blockingCall() {
        val sequence = sequenceOf(1, 2, 3)
            .map {
                println("Make blocking call to API")
                Thread.sleep(3000)
                it + 1
            }
            .toList()
        println("Sequence: $sequence")
    }
}
