package com.hiddewieringa.elevator.domain.person.model

import com.hiddewieringa.elevator.domain.Identity
import java.util.*

data class PersonId(override val id: UUID) : Identity<UUID>(id)
