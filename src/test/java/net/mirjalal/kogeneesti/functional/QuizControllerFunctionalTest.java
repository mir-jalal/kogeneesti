package net.mirjalal.kogeneesti.functional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import net.mirjalal.kogeneesti.model.Question;
import net.mirjalal.kogeneesti.model.Quiz;
import net.mirjalal.kogeneesti.repository.AnswerRepository;
import net.mirjalal.kogeneesti.repository.QuestionRepository;
import net.mirjalal.kogeneesti.repository.QuizRepository;

@SpringBootTest
class QuizControllerFunctionalTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @BeforeEach
    void cleanDatabase() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void createQuizAndGetQuizShouldWork() throws Exception {
        mockMvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"Grammar Quiz\"" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Grammar Quiz"));

        Quiz quiz = quizRepository.findAll().getFirst();

        mockMvc.perform(get("/api/quizzes/{id}", quiz.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(quiz.getId().longValue()))
                .andExpect(jsonPath("$.name").value("Grammar Quiz"));
    }

    @Test
    void addListAndRemoveQuestionShouldWork() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Vocabulary Quiz");
        quiz = quizRepository.save(quiz);

        mockMvc.perform(post("/api/quizzes/{id}/questions", quiz.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"type\":\"TEXT\"," +
                                "\"question\":\"Translate cat\"," +
                                "\"options\":[" +
                                "{\"type\":\"TEXT\",\"answer\":\"kass\",\"correct\":true}," +
                                "{\"type\":\"TEXT\",\"answer\":\"koer\",\"correct\":false}" +
                                "]" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.question").value("Translate cat"));

        mockMvc.perform(get("/api/quizzes/{id}/questions", quiz.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value("Translate cat"));

        Question question = questionRepository.findAll().getFirst();

        mockMvc.perform(delete("/api/quizzes/{quizId}/questions/{questionId}", quiz.getId(), question.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/quizzes/{id}/questions", quiz.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" ).isArray());
    }

    @Test
    void getQuizByIdShouldReturn404WhenMissing() throws Exception {
        mockMvc.perform(get("/api/quizzes/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Quiz not found"));
    }
}
