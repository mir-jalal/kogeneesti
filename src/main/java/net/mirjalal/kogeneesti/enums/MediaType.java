package net.mirjalal.kogeneesti.enums;

public enum MediaType {
    TEXT("text"),
    IMAGE("image");

    private final String name;
    
    MediaType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
