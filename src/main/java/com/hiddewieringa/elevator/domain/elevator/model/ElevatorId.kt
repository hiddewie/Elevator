package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.Identity
import java.util.*

data class ElevatorId(override val id: UUID) : Identity<UUID>(id)
