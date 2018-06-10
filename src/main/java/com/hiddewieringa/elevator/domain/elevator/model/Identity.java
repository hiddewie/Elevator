package com.hiddewieringa.elevator.domain.elevator.model;

import java.io.Serializable;

public abstract class Identity<T extends Serializable> {

    private final T id;

    public Identity(T t) {
        this.id = t;
    }

    public T getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identity<?> identity = (Identity<?>) o;

        return id != null ? id.equals(identity.id) : identity.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
