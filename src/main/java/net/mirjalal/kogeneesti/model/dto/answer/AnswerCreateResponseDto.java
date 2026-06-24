package net.mirjalal.kogeneesti.model.dto.answer;

import java.math.BigInteger;

public record AnswerCreateResponseDto(
    BigInteger id,
    String type,
    String answer,
    boolean correct
) {
    
}
