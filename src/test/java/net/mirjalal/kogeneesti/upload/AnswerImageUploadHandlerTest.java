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
import net.mirjalal.kogeneesti.model.Answer;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;
import net.mirjalal.kogeneesti.repository.AnswerRepository;

@ExtendWith(MockitoExtension.class)
class AnswerImageUploadHandlerTest {

    @Mock
    private AnswerRepository repository;

    @Test
    void methodsShouldDelegateToRepository() {
        AnswerImageUploadHandler handler = new AnswerImageUploadHandler(repository);
        Answer answer = new Answer();

        when(repository.getReferenceById(BigInteger.ONE)).thenReturn(answer);

        assertEquals(ImageType.ANSWER, handler.getImageType());
        ImageUpload loaded = handler.findImageUploadById(BigInteger.ONE);
        assertSame(answer, loaded);

        handler.saveImageUpload(answer);
        verify(repository).save(answer);
    }
}
