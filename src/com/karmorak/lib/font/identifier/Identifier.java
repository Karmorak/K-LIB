package com.karmorak.lib.font.identifier;

sealed public interface Identifier permits CharIdentifier, StringIdentifier {
    int getInt();
    String toString();
}

