package net.mirjalal.kogeneesti.model.dto.quiz;

import java.math.BigInteger;

public record QuizCreateResponseDto(
    BigInteger id,
    String name
) {}
