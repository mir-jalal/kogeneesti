package net.mirjalal.kogeneesti.model.dto.word;

import jakarta.validation.constraints.NotBlank;

public record WordCreateRequestDto(
    @NotBlank String word,
    @NotBlank String translation
) {}
