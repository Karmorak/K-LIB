package com.karmorak.lib.font.identifier;

import java.util.Objects;

public record StringIdentifier(String value) implements Identifier {
    @Override
    public int getInt() {
        return -1;
    }
    @Override
    public String toString() {
        return value();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringIdentifier(String value1))) return false;
        return Objects.equals(value(), value1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value());
    }
}
