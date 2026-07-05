package net.mirjalal.kogeneesti.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;
import net.mirjalal.kogeneesti.model.mapper.DictionaryMapper;
import net.mirjalal.kogeneesti.repository.DictionaryRepository;

@ExtendWith(MockitoExtension.class)
public class DictionaryServiceImplTest {

    @Mock
    private DictionaryRepository dictionaryRepository;

    @Mock
    private DictionaryMapper dictionaryMapper;

    @InjectMocks
    private DictionaryServiceImpl dictionaryService;

    @Test
    void createDictionaryShouldPersistAndReturnDto() {
        String name = "Basics";
        DictionaryCreateRequestDto request = new DictionaryCreateRequestDto(name);
        Dictionary dictionary = new Dictionary();
        dictionary.setId(BigInteger.ONE);
        dictionary.setName(name);
        DictionaryGetResponseDto expected = new DictionaryGetResponseDto(BigInteger.ONE, name, 0);

        when(dictionaryMapper.toEntity(request)).thenReturn(dictionary);
        when(dictionaryMapper.toGetResponseDto(dictionary)).thenReturn(expected);
        when(dictionaryRepository.save(any(Dictionary.class))).thenReturn(dictionary);

        DictionaryGetResponseDto actual = dictionaryService.createDictionary(request);

        assertEquals(expected, actual);
        verify(dictionaryRepository).save(dictionary);
    }

    @Test
    void getDictionaryByIdShouldMapEntity() {
        BigInteger id = BigInteger.ONE;
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        String name = "Basics";
        dictionary.setName(name);
        DictionaryWordsGetResponseDto expected = new DictionaryWordsGetResponseDto(id, name, 0, java.util.List.of());

        when(dictionaryRepository.findById(id)).thenReturn(Optional.of(dictionary));
        when(dictionaryMapper.toWordsGetResponseDto(dictionary)).thenReturn(expected);

        DictionaryWordsGetResponseDto actual = dictionaryService.getDictionaryById(id);

        assertEquals(expected, actual);
    }

    @Test
    void getDictionaryByIdShouldThrowWhenMissing() {
        BigInteger missingId = BigInteger.TEN;
        when(dictionaryRepository.findById(missingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> dictionaryService.getDictionaryById(missingId));

        assertEquals("Dictionary not found", exception.getMessage());
    }

    @Test
    void getAllDictionariesShouldMapAllEntities() {
        Dictionary first = new Dictionary();
        first.setId(BigInteger.ONE);
        first.setName("Basics");

        Dictionary second = new Dictionary();
        second.setId(BigInteger.TWO);
        second.setName("Advanced");

        DictionaryGetResponseDto firstDto = new DictionaryGetResponseDto(BigInteger.ONE, "Basics", 0);
        DictionaryGetResponseDto secondDto = new DictionaryGetResponseDto(BigInteger.TWO, "Advanced", 0);

        when(dictionaryRepository.findAll()).thenReturn(List.of(first, second));
        when(dictionaryMapper.toGetResponseDto(first)).thenReturn(firstDto);
        when(dictionaryMapper.toGetResponseDto(second)).thenReturn(secondDto);

        List<DictionaryGetResponseDto> result = dictionaryService.getAllDictionaries();

        assertEquals(List.of(firstDto, secondDto), result);
    }

    @Test
    void deleteDictionaryShouldLoadAndDeleteEntity() {
        BigInteger id = BigInteger.valueOf(9);
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        dictionary.setName("Removable");

        when(dictionaryRepository.findById(eq(id))).thenReturn(Optional.of(dictionary));

        dictionaryService.deleteDictionary(id);

        verify(dictionaryRepository).delete(dictionary);
    }
}
