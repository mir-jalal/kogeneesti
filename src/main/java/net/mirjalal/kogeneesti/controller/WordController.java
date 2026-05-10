package net.mirjalal.kogeneesti.controller;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.WordGetDto;
import net.mirjalal.kogeneesti.service.WordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/word")
public class WordController {

    private final WordService wordService;

    @PutMapping("/{id}")
    public ResponseEntity<WordGetDto> editWord(@PathVariable BigInteger id, String word, String translation) {
        WordGetDto wordDto = wordService.editWord(id, word, translation);
        return ResponseEntity.ok().body(wordDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDictionary(@PathVariable BigInteger id) {
        wordService.deleteWord(id);
        return ResponseEntity.noContent().build();
    }
    
}
