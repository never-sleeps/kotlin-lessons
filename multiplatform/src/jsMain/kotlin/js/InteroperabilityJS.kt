package js

import org.w3c.dom.Window
// обёртка над Window JS API
external val window: Window

val windowLocation = window.location

// вызов JS-кода через функцию js, неудобно для сопровождения
fun useMathRound(value: Double) = js("Math.round(value)")

// использование аннотаций для JS модулей
@JsModule("is-sorted")
@JsNonModule
external fun <T> sorted(array: Array<T>): Boolean

// использование оберток для JS модулей.
// Можно сгенерировать из TS, используя dukat (jsGenerateExternals).
@JsModule("js-big-decimal")
@JsNonModule
@JsName("bigDecimal")
external class JsBigDecimal(value: Any) {
    fun getValue(): String
    fun getPrettyValue(digits: Int, separator: String)
    fun round(precision: Int = definedExternally, mode: dynamic = definedExternally): JsBigDecimal
    companion object {
        fun getPrettyValue(number: Any, digits: Int, separator: String)
    }
}
