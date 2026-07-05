package net.mirjalal.kogeneesti.functional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.repository.DictionaryRepository;
import net.mirjalal.kogeneesti.repository.WordRepository;

@SpringBootTest
class WordControllerFunctionalTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private WordRepository wordRepository;

    @BeforeEach
    void cleanDatabase() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        wordRepository.deleteAll();
        dictionaryRepository.deleteAll();
    }

    @Test
    void getWordShouldReturnExpectedPayload() throws Exception {
        Word word = createWord("cat", "kass");

        mockMvc.perform(get("/api/words/{id}", word.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(word.getId().longValue()))
                .andExpect(jsonPath("$.word").value("cat"))
                .andExpect(jsonPath("$.translation").value("kass"))
                .andExpect(jsonPath("$.dictionaryId").value(word.getDictionary().getId().longValue()));
    }

    @Test
    void editWordShouldUpdateWordAndReturnUpdatedPayload() throws Exception {
        Word word = createWord("cat", "kass");

        mockMvc.perform(put("/api/words/{id}", word.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"word\":\"dog\"," +
                                "\"translation\":\"koer\"" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(word.getId().longValue()))
                .andExpect(jsonPath("$.word").value("dog"))
                .andExpect(jsonPath("$.translation").value("koer"));
    }

    @Test
    void deleteWordShouldReturnNoContentAndRemoveEntity() throws Exception {
        Word word = createWord("water", "vesi");

        mockMvc.perform(delete("/api/words/{id}", word.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/words/{id}", word.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Word not found"));
    }

    private Word createWord(String source, String translation) {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("Basics");
        dictionary = dictionaryRepository.save(dictionary);

        Word word = new Word();
        word.setWord(source);
        word.setTranslation(translation);
        word.setDictionary(dictionary);
        return wordRepository.save(word);
    }
}
