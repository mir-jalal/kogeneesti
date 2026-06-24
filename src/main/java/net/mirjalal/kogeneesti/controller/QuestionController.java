package net.mirjalal.kogeneesti.controller;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;
import net.mirjalal.kogeneesti.service.QuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<QuestionGetResponseDto> generateQuestion(@RequestParam(required = true) @Valid BigInteger dictionaryId, @RequestParam(required = false, defaultValue = "normal") String type) {
        return ResponseEntity.ok().body(questionService.generateQuestion(dictionaryId, type));
    }
}
