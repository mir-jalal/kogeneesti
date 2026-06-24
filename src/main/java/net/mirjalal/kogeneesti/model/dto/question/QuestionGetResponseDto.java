package net.mirjalal.kogeneesti.model.dto.question;

import java.util.List;

import net.mirjalal.kogeneesti.model.dto.answer.AnswerGetResponseDto;

public record QuestionGetResponseDto(
    String question,
    List<AnswerGetResponseDto> answers
) {

}
