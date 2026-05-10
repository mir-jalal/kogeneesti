package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.model.dto.WordGetDto;
import net.mirjalal.kogeneesti.repository.WordRepository;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    
    public WordGetDto createWord(String word, String translation, Dictionary dictionary) {
        Word newWord = new Word();
        newWord.setWord(word);
        newWord.setTranslation(translation);
        newWord.setDictionary(dictionary);

        wordRepository.save(newWord);

        return new WordGetDto(
            newWord.getWord(), 
            newWord.getTranslation(), 
            newWord.getDictionaryId()
        );
    }

    public void deleteWord(BigInteger id) {
        wordRepository.deleteById(id);
    }

    public WordGetDto editWord(BigInteger id, String word, String translation) {
        Word existingWord = wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
        if (word != null) {
            existingWord.setWord(word);
        }
        if (translation != null) {
            existingWord.setTranslation(translation);
        }
        wordRepository.save(existingWord);
        return new WordGetDto(
            existingWord.getWord(), 
            existingWord.getTranslation(), 
            existingWord.getDictionaryId()
        );
    }
}
