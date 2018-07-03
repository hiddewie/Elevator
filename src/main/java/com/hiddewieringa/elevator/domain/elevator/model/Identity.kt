package com.hiddewieringa.elevator.domain.elevator.model

abstract class Identity<T>(val id: T) {

    override fun toString(): String {
        return id.toString()
    }
}
