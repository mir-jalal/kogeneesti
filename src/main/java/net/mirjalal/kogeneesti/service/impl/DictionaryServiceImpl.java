package net.mirjalal.kogeneesti.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;
import net.mirjalal.kogeneesti.model.mapper.DictionaryMapper;
import net.mirjalal.kogeneesti.repository.DictionaryRepository;
import net.mirjalal.kogeneesti.service.DictionaryService;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryMapper dictionaryMapper;

    @Override
    public DictionaryGetResponseDto createDictionary(DictionaryCreateRequestDto dictionaryCreateDto) {
        Dictionary dictionary = dictionaryMapper.toEntity(dictionaryCreateDto);

        dictionaryRepository.save(dictionary);

        return dictionaryMapper.toGetResponseDto(dictionary);
    }

    @Override
    public DictionaryWordsGetResponseDto getDictionaryById(BigInteger id) {
        Dictionary dictionary = getDictionaryEntity(id);
        
        return dictionaryMapper.toWordsGetResponseDto(dictionary);
    }

    @Override
    public Dictionary getDictionaryEntity(BigInteger id) {
        return dictionaryRepository
            .findById(id).orElseThrow(() -> new NotFoundException("Dictionary not found"));
    }

    @Override
    public List<DictionaryGetResponseDto> getAllDictionaries() {
        return dictionaryRepository.findAll().stream()
            .map(dictionaryMapper::toGetResponseDto)
            .toList();
    }

    @Override
    public void deleteDictionary(BigInteger id) {
        Dictionary dictionary = getDictionaryEntity(id);
        
        dictionaryRepository.delete(dictionary);
    }
}
