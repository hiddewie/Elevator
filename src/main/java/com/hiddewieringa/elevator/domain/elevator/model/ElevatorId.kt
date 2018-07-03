package com.hiddewieringa.elevator.domain.elevator.model

import javax.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

class ElevatorId(id: UUID) : Identity<UUID>(id)
