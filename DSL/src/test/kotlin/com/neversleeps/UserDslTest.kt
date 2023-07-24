package com.neversleeps

import com.neversleeps.user.dsl.buildUser
import com.neversleeps.user.models.Action
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserDslTest {
    @Test
    fun `dsl user`() {
        val user = buildUser {
            name {
                first = "John"
                last = "Snow"
            }
            contacts {
                email = "email@gmail.com"
                phone = "18002000600"
            }
            actions {
                add(Action.UPDATE)
                add(Action.ADD)

                +Action.DELETE
                +Action.READ
            }
            availability {
                monday("10:00")
                friday("12:00")
            }
        }

        assertTrue(user.id.isNotEmpty())
        assertEquals("John", user.firstName)
        assertEquals("", user.secondName)
        assertEquals("Snow", user.lastName)
        assertEquals("email@gmail.com", user.email)
        assertEquals("18002000600", user.phone)
        assertEquals(setOf(Action.UPDATE, Action.ADD, Action.DELETE, Action.READ), user.actions)
        assertEquals(2, user.available.size)
    }
}
