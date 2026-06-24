package net.mirjalal.kogeneesti.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.Question;
import net.mirjalal.kogeneesti.model.Quiz;
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
import net.mirjalal.kogeneesti.service.QuizService;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;
    private final Random random;

    @Override
    public QuizCreateResponseDto createQuiz(QuizCreateRequestDto quizCreateRequestDto) {
        Quiz quiz = quizMapper.toEntity(quizCreateRequestDto);

        quizRepository.save(quiz);

        return quizMapper.toCreateResponseDto(quiz);
    }

    @Override
    public QuizGetResponseDto getQuizById(BigInteger id) {
        Quiz quiz = getQuizEntity(id);

        return quizMapper.toGetResponseDto(quiz);
    }

    @Override
    public void deleteQuiz(BigInteger id) {
        quizRepository.deleteById(id);
    }

    private Quiz getQuizEntity(BigInteger id) {
        return quizRepository.findById(id).orElseThrow(() -> new NotFoundException("Quiz not found"));
    }

    @Transactional
    @Override
    public QuestionCreateResponseDto addQuestionToQuiz(BigInteger id, QuestionCreateRequestDto request) {
        Question question = questionMapper.toEntity(request);
        question.setQuiz(getQuizEntity(id));
        question = questionRepository.save(question);

        request.options().stream()
                .map(questionMapper::toAnswerEntity)
                .forEach(question::addOption);

        Question savedQuestion = questionRepository.save(question);

        return questionMapper.toQuestionCreateResponseDto(savedQuestion);
    }

    @Override
    public List<QuizGetResponseDto> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();

        List<QuizGetResponseDto> quizDtos = quizzes.stream().map(quizMapper::toGetResponseDto).toList();
        
        return quizDtos;
    }

    @Override
    @Transactional
    public void removeQuestionFromQuiz(BigInteger quizId, BigInteger questionId) {
        Quiz quiz = getQuizEntity(quizId);

        Question question = quiz.getQuestionById(questionId);
        quiz.removeQuestion(question);
        questionRepository.delete(question);
    }

	@Override
	public List<QuestionCreateResponseDto> getQuestionsByQuizId(BigInteger id) {
        Quiz quiz = getQuizEntity(id);
        List<Question> questions = quiz.getQuestions();

        List<QuestionCreateResponseDto> questionDtos = questions.stream()
                .map(questionMapper::toQuestionCreateResponseDto)
                .toList();
        
        return questionDtos;	
    }

    //TODO: Implement a better algorithm for generating questions based on the dictionary and quiz
    @Override
    public QuestionGetResponseDto generateQuestion(BigInteger dictionaryId) {
        Quiz quiz = getQuizEntity(dictionaryId);
        
        List<Question> questions = quiz.getQuestions();

        if (questions.isEmpty()) {
            throw new IllegalStateException("Quiz is empty or does not have enough questions to generate a question");
        }

        Question question = questions.get(random.nextInt(questions.size()));
        
        return questionMapper.toGetResponseDto(question);
    }  
}
