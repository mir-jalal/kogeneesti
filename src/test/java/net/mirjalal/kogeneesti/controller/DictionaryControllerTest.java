package net.mirjalal.kogeneesti.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.service.DictionaryService;
import net.mirjalal.kogeneesti.service.WordService;

@ExtendWith(MockitoExtension.class)
class DictionaryControllerTest {

    @Mock
    private DictionaryService dictionaryService;

    @Mock
    private WordService wordService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        DictionaryController controller = new DictionaryController(dictionaryService, wordService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createDictionaryShouldReturnCreatedWithLocation() throws Exception {
        DictionaryGetResponseDto response = new DictionaryGetResponseDto(BigInteger.ONE, "Basics", 0);
        when(dictionaryService.createDictionary(new DictionaryCreateRequestDto("Basics"))).thenReturn(response);

        mockMvc.perform(post("/api/dictionaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Basics\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/dictionaries/1"))
                .andExpect(jsonPath("$.name").value("Basics"));
    }

    @Test
    void getDictionaryShouldReturnPayload() throws Exception {
        DictionaryWordsGetResponseDto response = new DictionaryWordsGetResponseDto(BigInteger.ONE, "Basics", 0, List.of());
        when(dictionaryService.getDictionaryById(BigInteger.ONE)).thenReturn(response);

        mockMvc.perform(get("/api/dictionaries/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Basics"));
    }

    @Test
    void getAllDictionariesShouldReturnArray() throws Exception {
        when(dictionaryService.getAllDictionaries()).thenReturn(List.of(
                new DictionaryGetResponseDto(BigInteger.ONE, "Basics", 1),
                new DictionaryGetResponseDto(BigInteger.TWO, "Travel", 0)));

        mockMvc.perform(get("/api/dictionaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Travel"));
    }

    @Test
    void deleteDictionaryShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/dictionaries/{id}", 3))
                .andExpect(status().isNoContent());

        verify(dictionaryService).deleteDictionary(BigInteger.valueOf(3));
    }

    @Test
    void addWordShouldReturnCreatedWithLocation() throws Exception {
        WordGetResponseDto response = new WordGetResponseDto(BigInteger.TEN, "cat", "kass", BigInteger.ONE);
        when(wordService.createWord(new WordCreateRequestDto("cat", "kass"), BigInteger.ONE)).thenReturn(response);

        mockMvc.perform(post("/api/dictionaries/{id}/words", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"cat\",\"translation\":\"kass\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/words/10"))
                .andExpect(jsonPath("$.id").value(10));
    }
}
