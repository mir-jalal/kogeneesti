package net.mirjalal.kogeneesti.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.mirjalal.kogeneesti.model.Quiz;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.quiz.QuizGetResponseDto;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questions", ignore = true)
    public Quiz toEntity(QuizCreateRequestDto quizCreateRequestDto);
    public QuizCreateResponseDto toCreateResponseDto(Quiz quiz);
    public QuizGetResponseDto toGetResponseDto(Quiz quiz);
}
