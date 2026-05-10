package net.mirjalal.kogeneesti.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.dto.DictionaryCreateDto;
import net.mirjalal.kogeneesti.model.dto.DictionaryGetDto;
import net.mirjalal.kogeneesti.model.dto.WordCreateDto;
import net.mirjalal.kogeneesti.model.dto.WordGetDto;
import net.mirjalal.kogeneesti.service.DictionaryService;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PostMapping
    public ResponseEntity<Dictionary> createDictionary(@RequestBody DictionaryCreateDto dictionaryCreateDto) {
        Dictionary createdDictionary = dictionaryService.createDictionary(dictionaryCreateDto.name());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdDictionary.getId()).toUri();
        return ResponseEntity.created(location).body(createdDictionary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dictionary> getDictionary(@PathVariable BigInteger id) {
        Dictionary dictionary = dictionaryService.getDictionary(id);
        if (dictionary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(dictionary);
    }

    @GetMapping
    public ResponseEntity<List<DictionaryGetDto>> getAllDictionaries() {
        return ResponseEntity.ok().body(dictionaryService.getAllDictionaries());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDictionary(@PathVariable BigInteger id) {
        dictionaryService.deleteDictionary(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordGetDto> addWord(@PathVariable BigInteger id, @RequestBody WordCreateDto wordCreateDto) {
        WordGetDto createdWord = dictionaryService.addWord(id, wordCreateDto.word(), wordCreateDto.translation());
        return ResponseEntity.ok().body(createdWord);
    }
}
