package net.mirjalal.kogeneesti.model.dto.answer;

public record AnswerCreateRequestDto(
    String type,
    String answer,
    boolean correct
) {
    
}
