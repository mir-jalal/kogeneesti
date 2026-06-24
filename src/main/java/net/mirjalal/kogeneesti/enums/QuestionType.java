package net.mirjalal.kogeneesti.enums;

public enum QuestionType {
    NORMAL("normal"),
    REVERSED("reversed"),
    MULTIPLE("multiple");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
