package com.hiddewieringa.elevator.domain.person

import com.hiddewieringa.elevator.domain.person.model.PersonId
import org.axonframework.commandhandling.TargetAggregateIdentifier

class PersonArrived(@field:TargetAggregateIdentifier val personId: PersonId, val floor: Long, val requestedFloor: Long)

class PersonEnteredElevator(@field:TargetAggregateIdentifier
                            val personId: PersonId)

class PersonLeftElevator(@field:TargetAggregateIdentifier
                         val personId: PersonId)
