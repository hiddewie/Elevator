package com.hiddewieringa.elevator.domain

abstract class Identity<T>(open val id: T) {

    override fun toString(): String {
        return id.toString()
    }
}
