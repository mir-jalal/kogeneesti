package net.mirjalal.kogeneesti.enums;

import java.util.Arrays;

public enum ImageType {
    ANSWER("answer"),
    QUESTION("question");

    private final String name;

    ImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ImageType from(String value) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown image type: " + value));
    }
}
