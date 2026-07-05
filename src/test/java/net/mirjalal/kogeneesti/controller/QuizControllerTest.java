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

import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizGetResponseDto;
import net.mirjalal.kogeneesti.service.QuizService;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private QuizService quizService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        QuizController controller = new QuizController(quizService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getQuizByIdShouldReturnNotFoundWhenNull() throws Exception {
        when(quizService.getQuizById(BigInteger.ONE)).thenReturn(null);

        mockMvc.perform(get("/api/quizzes/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getQuizByIdShouldReturnPayload() throws Exception {
        when(quizService.getQuizById(BigInteger.ONE)).thenReturn(new QuizGetResponseDto(BigInteger.ONE, "Quiz"));

        mockMvc.perform(get("/api/quizzes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Quiz"));
    }

    @Test
    void getAllQuizzesShouldReturnArray() throws Exception {
        when(quizService.getAllQuizzes()).thenReturn(List.of(new QuizGetResponseDto(BigInteger.ONE, "Quiz")));

        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void createQuizShouldReturnCreated() throws Exception {
        when(quizService.createQuiz(new QuizCreateRequestDto("Grammar")))
                .thenReturn(new QuizCreateResponseDto(BigInteger.TEN, "Grammar"));

        mockMvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Grammar\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/quizzes/10"))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void deleteQuizShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/quizzes/{id}", 8))
                .andExpect(status().isNoContent());

        verify(quizService).deleteQuiz(BigInteger.valueOf(8));
    }

    @Test
    void addAndListQuestionsShouldReturnCreatedAndOk() throws Exception {
        QuestionCreateResponseDto question = new QuestionCreateResponseDto(
                BigInteger.ONE,
                "TEXT",
                "Q",
                List.of(new AnswerCreateResponseDto(BigInteger.ONE, "TEXT", "A", true)));

        when(quizService.addQuestionToQuiz(
                BigInteger.ONE,
                new QuestionCreateRequestDto("TEXT", "Q", List.of(new AnswerCreateRequestDto("TEXT", "A", true)))))
                .thenReturn(question);
        when(quizService.getQuestionsByQuizId(BigInteger.ONE)).thenReturn(List.of(question));

        mockMvc.perform(post("/api/quizzes/{id}/questions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"TEXT\",\"question\":\"Q\",\"options\":[{\"type\":\"TEXT\",\"answer\":\"A\",\"correct\":true}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.question").value("Q"));

        mockMvc.perform(get("/api/quizzes/{id}/questions", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void removeQuestionShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/quizzes/{quizId}/questions/{questionId}", 5, 9))
                .andExpect(status().isNoContent());

        verify(quizService).removeQuestionFromQuiz(BigInteger.valueOf(5), BigInteger.valueOf(9));
    }
}
