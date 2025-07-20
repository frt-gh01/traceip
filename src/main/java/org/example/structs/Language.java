package org.example.structs;

public class Language {
    public String code;
    public String name;

    @Override
    public String toString() {
        return "%s (%s)".formatted(name, code);
    }
}
