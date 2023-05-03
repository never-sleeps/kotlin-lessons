@file:Suppress("PackageDirectoryMismatch")

package com.neversleeps.builders.kotlin

enum class Drink {
    WATER,
    COFFEE,
    TEA,
}

sealed class Meal {
    data class Breakfast(
        val eggs: Int,
        val bacon: Boolean,
        val drink: Drink,
        val title: String,
    ) : Meal()
}

class BreakfastBuilder {
    var eggs = 0
    var bacon = false
    var title = ""
    var drink = Drink.WATER

    fun build() = Meal.Breakfast(eggs, bacon, drink, title)
}

/**
 * block - лямбда-функция, которая расширяет билдер и я вляется extention'ом для BreakfastBuilder'а
 */
fun breakfast(block: BreakfastBuilder.() -> Unit): Meal.Breakfast {
    val builder = BreakfastBuilder()
    builder.block()
    return builder.build()

//    return BreakfastBuilder().apply(block).build() // эквивалентный вариант
}

fun main() {
    val meal = breakfast {
        eggs = 3
        drink = Drink.TEA
        bacon = true
        title = "breakfast with tea"
    }
    println(meal)
}
