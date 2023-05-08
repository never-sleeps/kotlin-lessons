import kotlinx.coroutines.delay

actual class CommonApiService {
    // асинхронный код с корутинами преобразуется в Promise
    actual suspend fun makeCall(): String {
        delay(1000)
        return "JS api call"
    }
}
