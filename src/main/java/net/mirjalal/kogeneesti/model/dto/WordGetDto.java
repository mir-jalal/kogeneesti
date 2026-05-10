package net.mirjalal.kogeneesti.model.dto;

import java.math.BigInteger;

public record WordGetDto(
    String word,
    String translation,
    BigInteger dictionaryId
) {}
