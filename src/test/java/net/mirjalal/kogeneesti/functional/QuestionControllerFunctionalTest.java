package net.mirjalal.kogeneesti.functional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.repository.DictionaryRepository;
import net.mirjalal.kogeneesti.repository.WordRepository;

@SpringBootTest
class QuestionControllerFunctionalTest {

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
    void generateNormalQuestionShouldReturnQuestionWithFourAnswers() throws Exception {
        Dictionary dictionary = createDictionaryWithWords();

        mockMvc.perform(get("/api/questions")
                        .param("dictionaryId", dictionary.getId().toString())
                        .param("type", "normal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").isString())
                .andExpect(jsonPath("$.answers").isArray())
                .andExpect(jsonPath("$.answers.length()").value(4));
    }

    @Test
    void generateQuestionShouldReturn404WhenDictionaryMissing() throws Exception {
        mockMvc.perform(get("/api/questions")
                        .param("dictionaryId", "999999")
                        .param("type", "normal"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Dictionary not found"));
    }

    private Dictionary createDictionaryWithWords() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("Basics");
        dictionary = dictionaryRepository.save(dictionary);

        List<Word> words = List.of(
                createWord(dictionary, "cat", "kass"),
                createWord(dictionary, "dog", "koer"),
                createWord(dictionary, "house", "maja"),
                createWord(dictionary, "water", "vesi"));

        wordRepository.saveAll(words);
        return dictionary;
    }

    private Word createWord(Dictionary dictionary, String source, String translation) {
        Word word = new Word();
        word.setWord(source);
        word.setTranslation(translation);
        word.setDictionary(dictionary);
        return word;
    }
}
