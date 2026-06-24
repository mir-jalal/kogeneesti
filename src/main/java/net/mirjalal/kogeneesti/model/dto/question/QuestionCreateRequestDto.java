package net.mirjalal.kogeneesti.model.dto.question;

import java.util.List;

import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateRequestDto;

public record QuestionCreateRequestDto(
    String type,
    String question,
    List<AnswerCreateRequestDto> options
) {}
