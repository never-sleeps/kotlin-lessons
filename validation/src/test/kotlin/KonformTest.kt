import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.pattern
import kotlinx.datetime.* // ktlint-disable no-wildcard-imports
import org.junit.Test
import kotlin.time.Duration.Companion.days

class KonformTest {
    private val objValidator = Validation<SomeObjectForKonformValidationTest> {
        SomeObjectForKonformValidationTest::userId {
            pattern("^[0-9a-zA-Z_-]{1,64}\$")
        }
        SomeObjectForKonformValidationTest::date {
            minAge(15)
        }
    }

    @Test
    fun `konform test for empty object`() {
        val obj = SomeObjectForKonformValidationTest()
        val resultUserId: ValidationResult<SomeObjectForKonformValidationTest> = objValidator.validate(obj)
        println(resultUserId.errors)
        // [ValidationError(dataPath=.userId, message=must match the expected pattern), ValidationError(dataPath=.date, message=Age cannot be less then 15 or null)]
    }

    @Test
    fun `konform test for age validation`() {
        val resultAge = objValidator.validate(
            SomeObjectForKonformValidationTest(
                userId = "987987987",
                date = Clock.System.now()
                    .minus((-5 * 365).days)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date,
            ),
        )
        println(resultAge.errors)
        // [ValidationError(dataPath=.date, message=Age cannot be less then 15 or null)]
    }

    @Test
    fun `konform test for successful validation`() {
        val resultOk = objValidator.validate(
            SomeObjectForKonformValidationTest(
                userId = "987987987",
                date = Clock.System.now()
                    .minus((-20 * 365).days)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date,
            ),
        )
        println(resultOk.errors)
        // []
    }
}

private fun ValidationBuilder<LocalDate?>.minAge(min: Int) = addConstraint(
    errorMessage = "Age cannot be less then $min or null",
) {
    if (it == null) {
        false
    } else {
        val currentDate = Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val realAge = (it - currentDate).years

        realAge >= min
    }
}

private data class SomeObjectForKonformValidationTest(
    val userId: String = "",
    val date: LocalDate? = null,
)
