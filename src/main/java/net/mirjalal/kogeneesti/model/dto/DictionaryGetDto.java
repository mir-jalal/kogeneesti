package net.mirjalal.kogeneesti.model.dto;

import java.math.BigInteger;

public record DictionaryGetDto (
    BigInteger id,
    String name,
    Integer wordCount
) {}
