import jakarta.validation.* // ktlint-disable no-wildcard-imports
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import kotlinx.datetime.* // ktlint-disable no-wildcard-imports
import me.neversleeps.validation.hibernate.CheckMinAge
import org.junit.Test
import kotlin.time.Duration.Companion.days

class HibernateValidationTest {
    private val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.validator

    @Test
    fun `hibernate test for empty object`() {
        val obj = SomeObjectForHibernateValidationTest()
        val errors = validator.validate(obj)
        println("=== result:\n" + errors.joinToString("\n"))
        // === result
        // ConstraintViolationImpl{interpolatedMessage='не должно равняться null', propertyPath=date, rootBeanClass=class SomeObject, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}
        // ConstraintViolationImpl{interpolatedMessage='должно соответствовать "^[0-9a-zA-Z_-]{1,64}$"', propertyPath=userId, rootBeanClass=class SomeObject, messageTemplate='{jakarta.validation.constraints.Pattern.message}'}
    }

    @Test
    fun `hibernate test for not valid object`() {
        val obj = SomeObjectForHibernateValidationTest(
            userId = "123",
            date = Clock.System.now()
                .minus((5 * 365).days)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date,
        )
        val errors2 = validator.validate(obj)
        println("now: ${Clock.System.now()}")
        println("=== result:\n" + errors2.joinToString("\n"))
        // === result
        // ConstraintViolationImpl{interpolatedMessage='Age less then '15'', propertyPath=date, rootBeanClass=class SomeObject, messageTemplate='Age less then '{minAge}''}
    }

    @Test
    fun `hibernate test for valid object`() {
        val obj = SomeObjectForHibernateValidationTest(
            userId = "123",
            date = Clock.System.now()
                .minus((20 * 365).days)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date,
        )
        val errors3 = validator.validate(obj)
        println("=== result:\n" + errors3.joinToString("\n"))
        // === result:
    }
}

private data class SomeObjectForHibernateValidationTest(
    @field:Pattern(regexp = "^[0-9a-zA-Z_-]{1,64}$")
    val userId: String = "",
    @field:NotNull
    @field:CheckMinAge(minAge = 15)
    val date: LocalDate? = null,
)
