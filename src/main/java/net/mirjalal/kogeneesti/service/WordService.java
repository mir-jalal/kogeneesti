package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;

import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;

public interface WordService {
    public WordGetResponseDto createWord(WordCreateRequestDto wordCreateRequestDto, BigInteger dictionaryId);
    public WordGetResponseDto getWord(BigInteger id);
    public WordGetResponseDto editWord(BigInteger id, WordCreateRequestDto wordCreateRequestDto);
    public void deleteWord(BigInteger id);
}
