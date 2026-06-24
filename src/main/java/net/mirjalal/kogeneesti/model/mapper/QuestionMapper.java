package net.mirjalal.kogeneesti.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.mirjalal.kogeneesti.model.Answer;
import net.mirjalal.kogeneesti.model.Question;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionCreateResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    @Mapping(target = "options", ignore = true)
    public Question toEntity(QuestionCreateRequestDto request);

    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    public Answer toAnswerEntity(AnswerCreateRequestDto dto);

    public QuestionCreateResponseDto toQuestionCreateResponseDto(Question question);
    
    public AnswerCreateResponseDto toAnswerCreateResponseDto(Answer answer);
    public AnswerGetResponseDto toAnswerGetResponseDto(Answer answer);
    
    @Mapping(target = "answers", source = "options")
    public QuestionGetResponseDto toGetResponseDto(Question question);

    default String mapAnswer(Answer answer) {
        return answer.getAnswer();
    }
}
