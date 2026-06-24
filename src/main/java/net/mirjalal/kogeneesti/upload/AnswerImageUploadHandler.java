package net.mirjalal.kogeneesti.upload;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.Answer;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;
import net.mirjalal.kogeneesti.repository.AnswerRepository;

@Component
@RequiredArgsConstructor
public class AnswerImageUploadHandler implements ImageUploadHandler {

    private final AnswerRepository repository;

    @Override
    public ImageType getImageType() {
        return ImageType.ANSWER;
    }

    @Override
    public ImageUpload findImageUploadById(BigInteger id) {
        return repository.getReferenceById(id);
    }

    @Override
    public void saveImageUpload(ImageUpload imageUpload) {
        repository.save((Answer) imageUpload);
    }
}
