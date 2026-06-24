package net.mirjalal.kogeneesti.upload;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;
import net.mirjalal.kogeneesti.model.Question;
import net.mirjalal.kogeneesti.repository.QuestionRepository;

@Component
@RequiredArgsConstructor
public class QuestionImageUploadHandler implements ImageUploadHandler {
    private final QuestionRepository questionRepository;
    @Override
    public ImageType getImageType() {
        return ImageType.QUESTION;
    }

    @Override
    public ImageUpload findImageUploadById(BigInteger id) {
        return questionRepository.getReferenceById(id);
    }

    @Override
    public void saveImageUpload(ImageUpload imageUpload) {
        questionRepository.save((Question) imageUpload);
    }
    
}
