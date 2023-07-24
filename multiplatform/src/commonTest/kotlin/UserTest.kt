import kotlin.test.Test
import kotlin.test.assertEquals

// запуск тестов для различных платформ (./gradlew clean jsTest)
//  преобразованные под конкретную платформу артефакты лежат :build/classes/kotlin, build/klib/cache/main
class UserTest {

    @Test
    fun test1() {
        val user = User("1", "Ivan", 24)
        assertEquals("1", user.id)
        assertEquals("Ivan", user.name)
        assertEquals(24, user.age)
    }
}
