package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;
import java.util.List;

import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;

public interface DictionaryService {
    public Dictionary getDictionaryEntity(BigInteger id);
    public DictionaryGetResponseDto createDictionary(DictionaryCreateRequestDto dictionaryCreateDto);
    public DictionaryWordsGetResponseDto getDictionaryById(BigInteger id);
    public List<DictionaryGetResponseDto> getAllDictionaries();
    public void deleteDictionary(BigInteger id);
}
