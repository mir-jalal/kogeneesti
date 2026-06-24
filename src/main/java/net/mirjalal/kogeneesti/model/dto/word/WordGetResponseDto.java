package net.mirjalal.kogeneesti.model.dto.word;

import java.math.BigInteger;

public record WordGetResponseDto(
    BigInteger id,
    String word,
    String translation,
    BigInteger dictionaryId
) {}
