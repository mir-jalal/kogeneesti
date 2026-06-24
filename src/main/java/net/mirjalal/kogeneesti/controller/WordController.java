package net.mirjalal.kogeneesti.controller;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.service.WordService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    @GetMapping("/{id}")
    public ResponseEntity<WordGetResponseDto> getWord(@PathVariable BigInteger id) {
        WordGetResponseDto word = wordService.getWord(id);
        return ResponseEntity.ok().body(word);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<WordGetResponseDto> editWord(@PathVariable BigInteger id, @RequestBody WordCreateRequestDto WordCreateRequestDto) {
        WordGetResponseDto existingWord = wordService.editWord(id, WordCreateRequestDto);
        return ResponseEntity.ok().body(existingWord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDictionary(@PathVariable BigInteger id) {
        wordService.deleteWord(id);
        return ResponseEntity.noContent().build();
    }
}
