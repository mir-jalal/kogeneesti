package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;

import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;

public interface QuestionService {

    public QuestionGetResponseDto generateQuestion(BigInteger dictionaryId, String type);
}
