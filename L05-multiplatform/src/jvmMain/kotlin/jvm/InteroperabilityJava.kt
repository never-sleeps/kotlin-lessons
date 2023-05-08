package jvm

// пример класса с аннотациями для преобразования в Java-классы
class InteroperabilityJava {

    @JvmName("customName") // customName будет использоваться при вызове этого метода из java
    fun `asd asd`() = "JVM: someFunction"

    // при использовании аннотации @JvmOverloads получим кучу перегрузок на использование параметров по умолчанию
    // без этой аннотации при вызове из java будет доступен только метод со всеми параметрами
    @JvmOverloads
    fun defaults(param1: String = "param1-default-val", param2: Int = 1, param3: Boolean = false) =
        "param1 = $param1, param2 = $param2, param3 = $param3"

    companion object {
        @JvmStatic
        fun functionOne() {
            println("InteroperabilityJava, method = functionOne")
        }
    }
}

// class MyClass(
//    @JvmField
//    val a: String = ""
// )
