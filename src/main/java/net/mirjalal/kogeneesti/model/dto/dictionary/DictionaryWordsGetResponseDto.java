package net.mirjalal.kogeneesti.model.dto.dictionary;

import java.math.BigInteger;
import java.util.List;

import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;

public record DictionaryWordsGetResponseDto (
    BigInteger id,
    String name,
    Integer wordCount,
    List<WordGetResponseDto> words
) {}
