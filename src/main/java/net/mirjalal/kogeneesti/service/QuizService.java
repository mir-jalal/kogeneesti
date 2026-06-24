package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;
import java.util.List;

import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizGetResponseDto;

public interface QuizService {
    public QuizCreateResponseDto createQuiz(QuizCreateRequestDto quizCreateRequestDto);
    public QuizGetResponseDto getQuizById(BigInteger id);
    public void deleteQuiz(BigInteger id);
    public QuestionCreateResponseDto addQuestionToQuiz(BigInteger id, QuestionCreateRequestDto request);
    public QuestionGetResponseDto generateQuestion(BigInteger dictionaryId);
    public List<QuizGetResponseDto> getAllQuizzes();
    public void removeQuestionFromQuiz(BigInteger quizId, BigInteger questionId);
    public List<QuestionCreateResponseDto> getQuestionsByQuizId(BigInteger id);
}
