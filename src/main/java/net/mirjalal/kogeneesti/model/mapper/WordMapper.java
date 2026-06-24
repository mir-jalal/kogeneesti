package net.mirjalal.kogeneesti.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;

@Mapper(componentModel = "spring")
public interface WordMapper {
    public WordGetResponseDto toGetResponseDto(Word word);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dictionary", ignore = true)
    public Word toEntity(WordCreateRequestDto wordCreateRequestDto);
}
