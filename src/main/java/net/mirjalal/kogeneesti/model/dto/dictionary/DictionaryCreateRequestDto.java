package net.mirjalal.kogeneesti.model.dto.dictionary;

import jakarta.validation.constraints.NotBlank;

public record DictionaryCreateRequestDto(
    @NotBlank String name
) {}
