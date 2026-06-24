package net.mirjalal.kogeneesti.service.impl;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.model.mapper.WordMapper;
import net.mirjalal.kogeneesti.repository.WordRepository;
import net.mirjalal.kogeneesti.service.DictionaryService;
import net.mirjalal.kogeneesti.service.WordService;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final WordMapper wordMapper;
    private final DictionaryService dictionaryService;
    
    @Override
    public WordGetResponseDto createWord(WordCreateRequestDto wordCreateRequestDto, BigInteger dictionaryId) {
        Word newWord = wordMapper.toEntity(wordCreateRequestDto);
        newWord.setDictionary(dictionaryService.getDictionaryEntity(dictionaryId));

        wordRepository.save(newWord);

        return wordMapper.toGetResponseDto(newWord);
    }

    @Override
    public void deleteWord(BigInteger id) {
        Word existingWord = getWordEntity(id);
        wordRepository.delete(existingWord);
    }

    @Override
    public WordGetResponseDto editWord(BigInteger id, WordCreateRequestDto wordCreateRequestDto) {
        Word existingWord = getWordEntity(id);
        
        Word updatedWord = wordMapper.toEntity(wordCreateRequestDto);
        updatedWord.setId(existingWord.getId());
        updatedWord.setDictionary(existingWord.getDictionary());

        wordRepository.save(updatedWord);
        return wordMapper.toGetResponseDto(updatedWord);
    }

    @Override
    public WordGetResponseDto getWord(BigInteger id) {
        Word word = getWordEntity(id);

        return wordMapper.toGetResponseDto(word);
    }

    private Word getWordEntity(BigInteger id) {
        return wordRepository.findById(id).orElseThrow(() -> new NotFoundException("Word not found"));
    }
}
