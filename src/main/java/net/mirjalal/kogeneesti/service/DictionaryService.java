package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.dto.DictionaryGetDto;
import net.mirjalal.kogeneesti.model.dto.WordGetDto;
import net.mirjalal.kogeneesti.repository.DictionaryRepository;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final WordService wordService;

    public Dictionary createDictionary(String name) {
        Dictionary dictionary = new Dictionary();
        dictionary.setName(name);

        this.dictionaryRepository.save(dictionary);

        return dictionary;
    }

    public Dictionary getDictionary(BigInteger id) {
        return dictionaryRepository.findById(id).orElse(null);
    }

    public WordGetDto addWord(BigInteger id, String word, String translation) {
        Dictionary dictionary = this.getDictionary(id);
        if (dictionary == null) {
            throw new IllegalArgumentException("Dictionary not found");
        }
        return wordService.createWord(word, translation, dictionary);
    }

    public List<DictionaryGetDto> getAllDictionaries() {
        return dictionaryRepository.findAll().stream()
            .map(dictionary -> new DictionaryGetDto(dictionary.getId(), dictionary.getName(), dictionary.getWords().size())).toList();
    }

    public void deleteDictionary(BigInteger id) {
        dictionaryRepository.deleteById(id);
    }
}
