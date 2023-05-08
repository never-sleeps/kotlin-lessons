import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonApiServiceJSTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test1() = runTest { // delay внутри runTest не будет отрабатывать
        assertEquals("JS api call", CommonApiService().makeCall())
    }
}
