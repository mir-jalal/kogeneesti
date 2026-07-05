package net.mirjalal.kogeneesti.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.mirjalal.kogeneesti.enums.ImageType;

class StringToImageTypeConverterTest {

    private final StringToImageTypeConverter converter = new StringToImageTypeConverter();

    @Test
    void convertShouldDelegateToImageTypeFrom() {
        assertEquals(ImageType.QUESTION, converter.convert("question"));
    }
}
