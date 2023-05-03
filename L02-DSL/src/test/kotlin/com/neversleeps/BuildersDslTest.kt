package com.neversleeps

import com.neversleeps.builders.java.BreakfastBuilder
import com.neversleeps.builders.kotlin.breakfast
import org.junit.Test
import kotlin.test.assertEquals
import com.neversleeps.builders.java.Drink as JavaDrink
import com.neversleeps.builders.kotlin.Drink as KotlinDrink

class BuildersDslTest {
    @Test
    fun `java breakfast builder test`() {
        val breakfast = BreakfastBuilder()
            .withEggs(3)
            .withBacon(true)
            .withTitle("Simple")
            .withDrink(JavaDrink.COFFEE)
            .build()

        assertEquals(3, breakfast.eggs)
        assertEquals(true, breakfast.bacon)
        assertEquals("Simple", breakfast.title)
        assertEquals(JavaDrink.COFFEE, breakfast.drink)
    }

    @Test
    fun `kotlin breakfast builder test`() {
        val breakfast = breakfast {
            eggs = 3 // BreakfastBuilder().eggs = 3
            bacon = true
            title = "Simple"
            drink = KotlinDrink.COFFEE
        }

        assertEquals(3, breakfast.eggs)
        assertEquals(true, breakfast.bacon)
        assertEquals("Simple", breakfast.title)
        assertEquals(KotlinDrink.COFFEE, breakfast.drink)
    }
}
