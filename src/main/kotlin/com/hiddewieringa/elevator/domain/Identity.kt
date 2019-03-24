package com.hiddewieringa.elevator.domain

import java.io.Serializable

abstract class Identity<T>(open val id: T): Serializable {

    override fun toString(): String {
        return id.toString()
    }
}
