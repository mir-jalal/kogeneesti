package net.mirjalal.kogeneesti.upload;

import java.math.BigInteger;

import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;

public interface ImageUploadHandler {
    ImageType getImageType();
    ImageUpload findImageUploadById(BigInteger id);
    void saveImageUpload(ImageUpload imageUpload);
}
