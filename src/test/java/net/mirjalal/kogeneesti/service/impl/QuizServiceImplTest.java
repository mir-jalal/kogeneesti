package net.mirjalal.kogeneesti.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.Answer;
import net.mirjalal.kogeneesti.model.Question;
import net.mirjalal.kogeneesti.model.Quiz;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizGetResponseDto;
import net.mirjalal.kogeneesti.model.mapper.QuestionMapper;
import net.mirjalal.kogeneesti.model.mapper.QuizMapper;
import net.mirjalal.kogeneesti.repository.QuestionRepository;
import net.mirjalal.kogeneesti.repository.QuizRepository;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizMapper quizMapper;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private Random random;

    @InjectMocks
    private QuizServiceImpl quizService;

    @Test
    void createQuizShouldPersistAndReturnDto() {
        QuizCreateRequestDto request = new QuizCreateRequestDto("Basics");
        Quiz quiz = new Quiz();
        quiz.setName("Basics");
        QuizCreateResponseDto expected = new QuizCreateResponseDto(BigInteger.ONE, "Basics");

        when(quizMapper.toEntity(request)).thenReturn(quiz);
        when(quizMapper.toCreateResponseDto(quiz)).thenReturn(expected);

        QuizCreateResponseDto actual = quizService.createQuiz(request);

        assertEquals(expected, actual);
        verify(quizRepository).save(quiz);
    }

    @Test
    void getQuizByIdShouldReturnMappedDto() {
        BigInteger id = BigInteger.ONE;
        Quiz quiz = new Quiz();
        quiz.setId(id);
        QuizGetResponseDto expected = new QuizGetResponseDto(id, "Quiz");

        when(quizRepository.findById(id)).thenReturn(Optional.of(quiz));
        when(quizMapper.toGetResponseDto(quiz)).thenReturn(expected);

        QuizGetResponseDto actual = quizService.getQuizById(id);

        assertEquals(expected, actual);
    }

    @Test
    void getQuizByIdShouldThrowWhenMissing() {
        BigInteger id = BigInteger.valueOf(99);
        when(quizRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> quizService.getQuizById(id));

        assertEquals("Quiz not found", exception.getMessage());
    }

    @Test
    void deleteQuizShouldDeleteById() {
        BigInteger id = BigInteger.TEN;

        quizService.deleteQuiz(id);

        verify(quizRepository).deleteById(id);
    }

    @Test
    void getAllQuizzesShouldMapAllEntities() {
        Quiz quiz1 = new Quiz();
        quiz1.setId(BigInteger.ONE);
        Quiz quiz2 = new Quiz();
        quiz2.setId(BigInteger.TWO);

        QuizGetResponseDto dto1 = new QuizGetResponseDto(BigInteger.ONE, "Q1");
        QuizGetResponseDto dto2 = new QuizGetResponseDto(BigInteger.TWO, "Q2");

        when(quizRepository.findAll()).thenReturn(List.of(quiz1, quiz2));
        when(quizMapper.toGetResponseDto(quiz1)).thenReturn(dto1);
        when(quizMapper.toGetResponseDto(quiz2)).thenReturn(dto2);

        List<QuizGetResponseDto> actual = quizService.getAllQuizzes();

        assertEquals(List.of(dto1, dto2), actual);
    }

    @Test
    void addQuestionToQuizShouldAttachOptionsAndReturnDto() {
        BigInteger quizId = BigInteger.ONE;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        Question mappedQuestion = new Question();
        Question persistedQuestion = new Question();
        persistedQuestion.setId(BigInteger.valueOf(5));
        persistedQuestion.setOptions(new ArrayList<>());
        persistedQuestion.setQuiz(quiz);

        AnswerCreateRequestDto option1 = new AnswerCreateRequestDto("text", "A", true);
        AnswerCreateRequestDto option2 = new AnswerCreateRequestDto("text", "B", false);
        QuestionCreateRequestDto request = new QuestionCreateRequestDto("text", "Question?", List.of(option1, option2));

        Answer answerEntity1 = new Answer();
        answerEntity1.setAnswer("A");
        Answer answerEntity2 = new Answer();
        answerEntity2.setAnswer("B");

        QuestionCreateResponseDto expected = new QuestionCreateResponseDto(
                BigInteger.valueOf(5),
                "text",
                "Question?",
                List.of(new AnswerCreateResponseDto(BigInteger.ONE, "text", "A", true)));

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(questionMapper.toEntity(request)).thenReturn(mappedQuestion);
        when(questionRepository.save(mappedQuestion)).thenReturn(persistedQuestion);
        when(questionMapper.toAnswerEntity(option1)).thenReturn(answerEntity1);
        when(questionMapper.toAnswerEntity(option2)).thenReturn(answerEntity2);
        when(questionRepository.save(persistedQuestion)).thenReturn(persistedQuestion);
        when(questionMapper.toQuestionCreateResponseDto(persistedQuestion)).thenReturn(expected);

        QuestionCreateResponseDto actual = quizService.addQuestionToQuiz(quizId, request);

        assertEquals(expected, actual);
        assertSame(quiz, mappedQuestion.getQuiz());
        assertEquals(2, persistedQuestion.getOptions().size());
        verify(questionRepository).save(mappedQuestion);
        verify(questionRepository).save(persistedQuestion);
    }

    @Test
    void removeQuestionFromQuizShouldDetachAndDeleteQuestion() {
        BigInteger quizId = BigInteger.valueOf(7);
        BigInteger questionId = BigInteger.valueOf(70);

        Question question = new Question();
        question.setId(questionId);
        question.setOptions(new ArrayList<>());

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setQuestions(new ArrayList<>(List.of(question)));
        question.setQuiz(quiz);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        quizService.removeQuestionFromQuiz(quizId, questionId);

        verify(questionRepository).delete(question);
        assertEquals(0, quiz.getQuestions().size());
    }

    @Test
    void getQuestionsByQuizIdShouldMapQuestions() {
        BigInteger quizId = BigInteger.valueOf(3);
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        Question q1 = new Question();
        q1.setId(BigInteger.ONE);
        Question q2 = new Question();
        q2.setId(BigInteger.TWO);
        quiz.setQuestions(List.of(q1, q2));

        QuestionCreateResponseDto dto1 = new QuestionCreateResponseDto(BigInteger.ONE, "text", "Q1", List.of());
        QuestionCreateResponseDto dto2 = new QuestionCreateResponseDto(BigInteger.TWO, "text", "Q2", List.of());

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(questionMapper.toQuestionCreateResponseDto(q1)).thenReturn(dto1);
        when(questionMapper.toQuestionCreateResponseDto(q2)).thenReturn(dto2);

        List<QuestionCreateResponseDto> actual = quizService.getQuestionsByQuizId(quizId);

        assertEquals(List.of(dto1, dto2), actual);
    }

    @Test
    void generateQuestionShouldReturnRandomMappedQuestion() {
        BigInteger quizId = BigInteger.ONE;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        Question q1 = new Question();
        q1.setId(BigInteger.ONE);
        Question q2 = new Question();
        q2.setId(BigInteger.TWO);
        quiz.setQuestions(List.of(q1, q2));

        QuestionGetResponseDto expected = new QuestionGetResponseDto("Q2", List.of());

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(random.nextInt(2)).thenReturn(1);
        when(questionMapper.toGetResponseDto(q2)).thenReturn(expected);

        QuestionGetResponseDto actual = quizService.generateQuestion(quizId);

        assertEquals(expected, actual);
    }

    @Test
    void generateQuestionShouldThrowWhenQuizHasNoQuestions() {
        BigInteger quizId = BigInteger.valueOf(5);
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setQuestions(List.of());

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> quizService.generateQuestion(quizId));

        assertEquals("Quiz is empty or does not have enough questions to generate a question", exception.getMessage());
    }
}
