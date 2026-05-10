package net.mirjalal.kogeneesti.model.dto;

import java.util.List;

public record QuestionDto(
    String question,
    String correctAnswer,
    List<String> answers
) {

}
