package net.mirjalal.kogeneesti.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.mirjalal.kogeneesti.model.dto.answer.AnswerGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.service.DictionaryService;
import net.mirjalal.kogeneesti.service.QuizService;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private Random random;

    @Mock
    private DictionaryService dictionaryService;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void generateQuestionShouldBuildNormalQuestion() {
        BigInteger dictionaryId = BigInteger.ONE;
        List<WordGetResponseDto> words = words();
        when(dictionaryService.getDictionaryById(dictionaryId))
                .thenReturn(new DictionaryWordsGetResponseDto(dictionaryId, "Basics", words.size(), words));
        when(random.nextInt(words.size())).thenReturn(2, 0, 1, 3);

        QuestionGetResponseDto question = questionService.generateQuestion(dictionaryId, "normal");

        assertEquals("water", question.question());
        assertEquals(4, question.answers().size());
        assertEquals(1, question.answers().stream().filter(responseDto -> responseDto.correct()).count());
        assertEquals("vesi", question.answers().stream().filter(responseDto -> responseDto.correct()).findFirst().orElseThrow().answer());
    }

    @Test
    void generateQuestionShouldBuildReversedQuestion() {
        BigInteger dictionaryId = BigInteger.TWO;
        List<WordGetResponseDto> words = words();
        when(dictionaryService.getDictionaryById(dictionaryId))
                .thenReturn(new DictionaryWordsGetResponseDto(dictionaryId, "Basics", words.size(), words));
        when(random.nextInt(words.size())).thenReturn(1, 0, 2, 3);

        QuestionGetResponseDto question = questionService.generateQuestion(dictionaryId, "reversed");

        assertEquals("koer", question.question());
        assertEquals(4, question.answers().size());
        assertEquals("dog", question.answers().stream().filter(responseDto -> responseDto.correct()).findFirst().orElseThrow().answer());
    }

    @Test
    void generateQuestionShouldDelegateToQuizServiceForMultipleChoice() {
        BigInteger dictionaryId = BigInteger.valueOf(9);
        QuestionGetResponseDto expected = new QuestionGetResponseDto("q", List.of(new AnswerGetResponseDto("a", true)));
        when(quizService.generateQuestion(dictionaryId)).thenReturn(expected);

        QuestionGetResponseDto actual = questionService.generateQuestion(dictionaryId, "multiple");

        assertEquals(expected, actual);
        verify(quizService).generateQuestion(dictionaryId);
    }

    @Test
    void generateQuestionShouldThrowWhenQuestionTypeIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> questionService.generateQuestion(BigInteger.ONE, "invalid"));

        assertEquals("Invalid question type", exception.getMessage());
    }

    @Test
    void generateQuestionShouldThrowWhenDictionaryHasLessThanFourWords() {
        BigInteger dictionaryId = BigInteger.ONE;
        List<WordGetResponseDto> words = List.of(
                new WordGetResponseDto(BigInteger.ONE, "cat", "kass", dictionaryId),
                new WordGetResponseDto(BigInteger.TWO, "dog", "koer", dictionaryId),
                new WordGetResponseDto(BigInteger.valueOf(3), "bird", "lind", dictionaryId));

        when(dictionaryService.getDictionaryById(dictionaryId))
                .thenReturn(new DictionaryWordsGetResponseDto(dictionaryId, "Small", words.size(), words));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> questionService.generateQuestion(dictionaryId, "normal"));

        assertEquals("Dictionary is empty or does not have enough words to generate a question", exception.getMessage());
    }

    private List<WordGetResponseDto> words() {
        BigInteger dictionaryId = BigInteger.ONE;
        return List.of(
                new WordGetResponseDto(BigInteger.ONE, "cat", "kass", dictionaryId),
                new WordGetResponseDto(BigInteger.TWO, "dog", "koer", dictionaryId),
                new WordGetResponseDto(BigInteger.valueOf(3), "water", "vesi", dictionaryId),
                new WordGetResponseDto(BigInteger.valueOf(4), "house", "maja", dictionaryId));
    }
}
