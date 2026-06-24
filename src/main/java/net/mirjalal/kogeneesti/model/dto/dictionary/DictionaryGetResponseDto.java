package net.mirjalal.kogeneesti.model.dto.dictionary;

import java.math.BigInteger;

public record DictionaryGetResponseDto (
    BigInteger id,
    String name,
    Integer wordCount
) {}
