package me.neversleeps.validation.hibernate

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import kotlinx.datetime.LocalDate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @PostMapping("/test")
    fun post(@Valid @RequestBody model: Model): String { // ktlint-disable annotation
        return "${model.userId}: ${model.date}"
    }
}

data class Model(
    @field:Pattern(regexp = "^[0-9a-zA-Z_-]{1,64}$")
    val userId: String = "",

    @field:NotNull
    @field:CheckMinAge(minAge = 15) // "2015-12-30"
    val date: LocalDate? = null,
)