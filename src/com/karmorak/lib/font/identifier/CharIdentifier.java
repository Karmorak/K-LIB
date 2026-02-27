package com.karmorak.lib.font.identifier;

import java.util.Objects;

public record CharIdentifier(char value) implements Identifier {
    @Override
    public int getInt() {
        return value();
    }
    @Override
    public String toString() {
        return "" + value();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CharIdentifier(char value1))) return false;
        return value() == value1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value());
    }
}
