package net.mirjalal.kogeneesti.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {
    @Mapping(target = "wordCount", source = "words")
    public DictionaryGetResponseDto toGetResponseDto(Dictionary dictionary);

    public DictionaryCreateRequestDto toCreateRequestDto(Dictionary dictionary);

    @Mapping(target = "wordCount", source = "words")
    public DictionaryWordsGetResponseDto toWordsGetResponseDto(Dictionary dictionary);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "words", ignore = true)
    public Dictionary toEntity(DictionaryCreateRequestDto dictionaryCreateDto);

    default Integer mapWordCount(List<Word> words) {
        return words != null ? words.size() : 0;
    }
}
