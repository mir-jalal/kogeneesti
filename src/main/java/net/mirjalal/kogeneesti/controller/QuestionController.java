package net.mirjalal.kogeneesti.controller;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.QuestionDto;
import net.mirjalal.kogeneesti.service.QuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/generate")
    public ResponseEntity<QuestionDto> generateQuestion(@RequestParam BigInteger dictionaryId) {
        return ResponseEntity.ok().body(questionService.generateQuestion(dictionaryId));
    }

    @GetMapping("/generate-reversed")
    public ResponseEntity<QuestionDto> generateReversedQuestion(@RequestParam BigInteger dictionaryId) {
        return ResponseEntity.ok().body(questionService.generateReversedQuestion(dictionaryId));
    }
}
