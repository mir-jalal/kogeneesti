package net.mirjalal.kogeneesti.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.mirjalal.kogeneesti.enums.ImageType;
import net.mirjalal.kogeneesti.model.dto.ImageUploadedResponseDto;
import net.mirjalal.kogeneesti.service.ImageService;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private ImageService imageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ImageController controller = new ImageController(imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void uploadImageShouldReturnOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "a.jpg", "image/jpeg", new byte[] {1, 2});
        ImageUploadedResponseDto response = new ImageUploadedResponseDto(BigInteger.ONE, "a.jpg", ImageType.QUESTION);
        when(imageService.uploadImage(BigInteger.ONE, ImageType.QUESTION, file)).thenReturn(response);

        mockMvc.perform(multipart("/api/images/{type}/{id}", "QUESTION", 1).file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("a.jpg"));
    }

    @Test
    void getImageShouldReturnJpegContentType() throws Exception {
        Resource resource = new ByteArrayResource(new byte[] {9, 8, 7});
        when(imageService.getImage("1")).thenReturn(resource);

        mockMvc.perform(get("/api/images/{type}/{id}", "QUESTION", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }
}
