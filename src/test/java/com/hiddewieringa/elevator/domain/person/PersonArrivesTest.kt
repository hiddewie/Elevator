package com.hiddewieringa.elevator.domain.person

import com.hiddewieringa.elevator.domain.person.model.PersonId
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class PersonArrivesTest {
    @Test
    fun arrives() {
        PersonArrives(PersonId(UUID.randomUUID()), 0, 1)
    }

    @Test
    fun arrivesInvalid() {
        try {
            PersonArrives(PersonId(UUID.randomUUID()), 0, 0)
            fail()
        } catch (e: IllegalArgumentException) {
        }
    }
}
