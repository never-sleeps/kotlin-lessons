package sequence

enum class Color {
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
}

enum class Shape {
    SQUARE,
    CIRCLE,
    TRIANGLE,
    RHOMBUS,
}

data class Figure(
    val color: Color,
    val shape: Shape,
)
