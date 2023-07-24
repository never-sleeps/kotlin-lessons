import kotlin.test.Test

class ServiceTest {

    @Test
    fun test1() {
        Service().doSomething() // printLn преобразуется в console.log для JS
    }
}
