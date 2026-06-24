package net.mirjalal.kogeneesti.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import net.mirjalal.kogeneesti.enums.ImageType;

@Component
public class StringToImageTypeConverter implements Converter<String, ImageType> {

    @Override
    public ImageType convert(String source) {
        return ImageType.from(source);
    }
}
