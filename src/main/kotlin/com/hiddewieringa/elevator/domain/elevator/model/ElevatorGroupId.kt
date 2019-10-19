package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.Identity
import java.util.UUID

data class ElevatorGroupId(override val id: UUID) : Identity<UUID>(id)
