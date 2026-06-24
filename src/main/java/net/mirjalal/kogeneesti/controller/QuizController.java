package net.mirjalal.kogeneesti.controller;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizGetResponseDto;
import net.mirjalal.kogeneesti.service.QuizService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    
    @GetMapping("/{id}")
    public ResponseEntity<QuizGetResponseDto> getQuizById(@PathVariable BigInteger id) {
        QuizGetResponseDto quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quiz);
    }

    @GetMapping
    public ResponseEntity<List<QuizGetResponseDto>> getAllQuizzes() {
        List<QuizGetResponseDto> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @PostMapping
    public ResponseEntity<QuizCreateResponseDto> createQuiz(@RequestBody QuizCreateRequestDto quizCreateRequestDto) {
        QuizCreateResponseDto quiz = quizService.createQuiz(quizCreateRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(quiz.id()).toUri();
        return ResponseEntity.status(201).location(location).body(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable BigInteger id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/questions")
    public ResponseEntity<QuestionCreateResponseDto> addQuestionToQuiz(@PathVariable BigInteger id, @RequestBody QuestionCreateRequestDto questionCreateRequestDto) {
        QuestionCreateResponseDto question = quizService.addQuestionToQuiz(id, questionCreateRequestDto);
        return ResponseEntity.status(201).body(question);
    }

    @DeleteMapping("/{quizId}/questions/{questionId}")
    public ResponseEntity<Void> removeQuestionFromQuiz(@PathVariable BigInteger quizId, @PathVariable BigInteger questionId) {
        quizService.removeQuestionFromQuiz(quizId, questionId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionCreateResponseDto>> getQuestionsByQuizId(@PathVariable BigInteger id) {
        List<QuestionCreateResponseDto> questions = quizService.getQuestionsByQuizId(id);
        return ResponseEntity.ok(questions);
    }
}
