package net.mirjalal.kogeneesti.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.service.DictionaryService;
import net.mirjalal.kogeneesti.service.WordService;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dictionaries")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final WordService wordService;

    @PostMapping
    public ResponseEntity<DictionaryGetResponseDto> createDictionary(@RequestBody @Valid DictionaryCreateRequestDto dictionaryCreateDto) {
        DictionaryGetResponseDto createdDictionary = dictionaryService.createDictionary(dictionaryCreateDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdDictionary.id()).toUri();
        return ResponseEntity.created(location).body(createdDictionary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DictionaryWordsGetResponseDto> getDictionary(@PathVariable BigInteger id) {
        DictionaryWordsGetResponseDto dictionary = dictionaryService.getDictionaryById(id);

        return ResponseEntity.ok().body(dictionary);
    }

    @GetMapping
    public ResponseEntity<List<DictionaryGetResponseDto>> getAllDictionaries() {
        return ResponseEntity.ok().body(dictionaryService.getAllDictionaries());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDictionary(@PathVariable BigInteger id) {
        dictionaryService.deleteDictionary(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/words")
    public ResponseEntity<WordGetResponseDto> addWord(@PathVariable BigInteger id, @RequestBody WordCreateRequestDto wordCreateRequestDto) {
        WordGetResponseDto createdWord = wordService.createWord(wordCreateRequestDto, id);

        URI location = ServletUriComponentsBuilder.fromPath("/api/words").path("/{id}")
                .buildAndExpand(createdWord.id()).toUri();

        return ResponseEntity.created(location).body(createdWord);
    }
}
