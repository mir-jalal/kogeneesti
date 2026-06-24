package net.mirjalal.kogeneesti.model.dto.question;

import java.math.BigInteger;
import java.util.List;

import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateResponseDto;

public record QuestionCreateResponseDto(
    BigInteger id,
    String type,
    String question,
    List<AnswerCreateResponseDto> options
) {}
