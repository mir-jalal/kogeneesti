package net.mirjalal.kogeneesti.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;
import net.mirjalal.kogeneesti.model.dto.ImageUploadedResponseDto;
import net.mirjalal.kogeneesti.storage.GeneralStorage;
import net.mirjalal.kogeneesti.upload.ImageUploadHandler;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private GeneralStorage storage;

    @Mock
    private ImageUploadHandler questionHandler;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ImageUpload imageUpload;

    @Mock
    private Resource resource;

    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        when(questionHandler.getImageType()).thenReturn(ImageType.QUESTION);
        imageService = new ImageServiceImpl(storage, List.of(questionHandler));
    }

    @Test
    void uploadImageShouldStoreFileAndPersistEntityWhenUploadTypeIsImage() {
        BigInteger id = BigInteger.ONE;
        when(questionHandler.findImageUploadById(id)).thenReturn(imageUpload);
        when(imageUpload.isImage()).thenReturn(true);
        when(storage.uploadFile(multipartFile)).thenReturn("question-1.jpg");

        ImageUploadedResponseDto response = imageService.uploadImage(id, ImageType.QUESTION, multipartFile);

        assertEquals(id, response.id());
        assertEquals("question-1.jpg", response.name());
        assertEquals(ImageType.QUESTION, response.imageType());
        verify(imageUpload).setFileName("question-1.jpg");
        verify(questionHandler).saveImageUpload(imageUpload);
    }

    @Test
    void uploadImageShouldThrowWhenNoHandlerExistsForType() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> imageService.uploadImage(BigInteger.ONE, ImageType.ANSWER, multipartFile));

        assertEquals("No handler found for image type: ANSWER", exception.getMessage());
    }

    @Test
    void uploadImageShouldThrowWhenEntityIsNotImage() {
        BigInteger id = BigInteger.TWO;
        when(questionHandler.findImageUploadById(id)).thenReturn(imageUpload);
        when(imageUpload.isImage()).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> imageService.uploadImage(id, ImageType.QUESTION, multipartFile));

        assertEquals("Are you scam?", exception.getMessage());
    }

    @Test
    void getImageShouldDelegateToStorage() {
        when(storage.serveFile("abc.png")).thenReturn(resource);

        Resource actual = imageService.getImage("abc.png");

        assertSame(resource, actual);
        verify(storage).serveFile("abc.png");
    }

    @Test
    void constructorShouldRejectEmptyHandlers() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ImageServiceImpl(storage, List.of()));

        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }
}
