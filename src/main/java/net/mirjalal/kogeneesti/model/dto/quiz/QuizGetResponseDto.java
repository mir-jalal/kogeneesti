package net.mirjalal.kogeneesti.model.dto.quiz;

import java.math.BigInteger;

public record QuizGetResponseDto(
    BigInteger id,
    String name
) {}
