package com.hiddewieringa.elevator.domain.elevator.query

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class FloorResult(val floor: Long?)
