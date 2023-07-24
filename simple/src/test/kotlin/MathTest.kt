import kotlin.test.Test
import kotlin.test.assertEquals

class MathTest {
    @Test
    fun testForMultiplication() {
        assertEquals(4, 2 * 2)
    }

    @Test
    fun testForDivision() {
        assertEquals(2, 4 / 2)
    }

    @Test
    fun testForAddition() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun testForDeduction() {
        assertEquals(1, 3 - 2)
    }
}
