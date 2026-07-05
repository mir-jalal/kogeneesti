package net.mirjalal.kogeneesti.functional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.mirjalal.kogeneesti.repository.DictionaryRepository;

@SpringBootTest
class DictionaryControllerFunctionalTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @BeforeEach
    void cleanDatabase() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        dictionaryRepository.deleteAll();
    }

    @Test
    void createAndGetDictionaryShouldReturnExpectedPayload() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/dictionaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"Estonian Basics\"" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Estonian Basics"))
                .andExpect(jsonPath("$.wordCount").value(0))
                .andReturn();

            String location = createResult.getResponse().getHeader("Location");

            mockMvc.perform(get(URI.create(location)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Estonian Basics"))
                .andExpect(jsonPath("$.wordCount").value(0))
                .andExpect(jsonPath("$.words").isArray());
    }

    @Test
    void getDictionaryByIdShouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/dictionaries/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Dictionary not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
