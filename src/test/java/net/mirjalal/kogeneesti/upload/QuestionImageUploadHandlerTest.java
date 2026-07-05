package net.mirjalal.kogeneesti.upload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.Question;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;
import net.mirjalal.kogeneesti.repository.QuestionRepository;

@ExtendWith(MockitoExtension.class)
class QuestionImageUploadHandlerTest {

    @Mock
    private QuestionRepository repository;

    @Test
    void methodsShouldDelegateToRepository() {
        QuestionImageUploadHandler handler = new QuestionImageUploadHandler(repository);
        Question question = new Question();

        when(repository.getReferenceById(BigInteger.ONE)).thenReturn(question);

        assertEquals(ImageType.QUESTION, handler.getImageType());
        ImageUpload loaded = handler.findImageUploadById(BigInteger.ONE);
        assertSame(question, loaded);

        handler.saveImageUpload(question);
        verify(repository).save(question);
    }
}
