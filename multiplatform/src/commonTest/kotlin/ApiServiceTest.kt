import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiServiceTest {

    // использование runTest вместо runBlocking для coroutine
    // (пропускает вызовы delay на всех платформах)
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test1() = runTest {
        assertEquals("Api call response", ApiService().call())
    }
}