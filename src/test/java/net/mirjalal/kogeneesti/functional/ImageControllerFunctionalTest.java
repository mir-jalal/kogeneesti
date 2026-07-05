package net.mirjalal.kogeneesti.functional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class ImageControllerFunctionalTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void uploadImageShouldReturn400ForInvalidImageType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "sample.jpg", "image/jpeg", "abc".getBytes());

        mockMvc.perform(multipart("/api/images/{type}/{id}", "invalid", "1").file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getImageShouldReturn400ForInvalidImageType() throws Exception {
        mockMvc.perform(get("/api/images/{type}/{id}", "invalid", "1"))
                .andExpect(status().isBadRequest());
    }
}
