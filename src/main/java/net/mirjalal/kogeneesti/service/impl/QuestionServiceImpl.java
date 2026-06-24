package net.mirjalal.kogeneesti.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.enums.QuestionType;
import net.mirjalal.kogeneesti.model.dto.answer.AnswerGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.dictionary.DictionaryWordsGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.question.QuestionGetResponseDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.service.DictionaryService;
import net.mirjalal.kogeneesti.service.QuestionService;
import net.mirjalal.kogeneesti.service.QuizService;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final static int NUMBER_OF_OPTIONS = 4;

    private final Random random;
    private final DictionaryService dictionaryService;
    private final QuizService quizService;

    //TODO: Implement a method to generate questions based on the type (normal, reversed, multiple choice)
    @Override
    public QuestionGetResponseDto generateQuestion(BigInteger dictionaryId, String type) {
        if (QuestionType.NORMAL.getValue().equals(type)) {
            return generateQuestion(dictionaryId);
        } else if (QuestionType.REVERSED.getValue().equals(type)) {
            return generateReversedQuestion(dictionaryId);
        } else if (QuestionType.MULTIPLE.getValue().equals(type)) {
            return generateMultipleChoiceQuestion(dictionaryId);
        }
        else {
            throw new IllegalArgumentException("Invalid question type");
        }
    }

    private QuestionGetResponseDto generateMultipleChoiceQuestion(BigInteger dictionaryId) {
        QuestionGetResponseDto question = quizService.generateQuestion(dictionaryId);
        return question;
    }

    
    public QuestionGetResponseDto generateQuestion(BigInteger dictionaryId) {
        List<WordGetResponseDto> words = this.getWordsByDictionaryId(dictionaryId);

        List<Integer> numbers = this.generateUniqueRandomNumbers(words.size());
        Iterator<Integer> iterator = numbers.iterator();
        
        WordGetResponseDto correctWord = words.get(iterator.next());
        List<AnswerGetResponseDto> options = new ArrayList<>();
        while (iterator.hasNext()) {
            options.add(new AnswerGetResponseDto(words.get(iterator.next()).translation(), false));
        }
        options.add(new AnswerGetResponseDto(correctWord.translation(), true));

        QuestionGetResponseDto questionDto = new QuestionGetResponseDto(
            correctWord.word(),
            options
        );

        return questionDto;
    }

    
    public QuestionGetResponseDto generateReversedQuestion(BigInteger dictionaryId) {
        List<WordGetResponseDto> words = this.getWordsByDictionaryId(dictionaryId);

        List<Integer> numbers = this.generateUniqueRandomNumbers(words.size());
        Iterator<Integer> iterator = numbers.iterator();
        
        WordGetResponseDto correctWord = words.get(iterator.next());
        List<AnswerGetResponseDto> options = new ArrayList<>();
        while (iterator.hasNext()) {
            options.add(new AnswerGetResponseDto(words.get(iterator.next()).word(), false));
        }
        options.add(new AnswerGetResponseDto(correctWord.word(), true));
        QuestionGetResponseDto questionDto = new QuestionGetResponseDto(
            correctWord.translation(),
            options
        );

        return questionDto;
    }
    
    private List<WordGetResponseDto> getWordsByDictionaryId(BigInteger dictionaryId) {
        DictionaryWordsGetResponseDto dictionary = dictionaryService.getDictionaryById(dictionaryId);

        List<WordGetResponseDto> words = dictionary.words();
        if (words.isEmpty() || words.size() < NUMBER_OF_OPTIONS) {
            throw new IllegalStateException("Dictionary is empty or does not have enough words to generate a question");
        }

        return words;
    }
    
    private List<Integer> generateUniqueRandomNumbers(int upperBound, int count) {
        List<Integer> numbers = new ArrayList<>();

        while (numbers.size() < count) {
            int value = random.nextInt(upperBound);

            if (!numbers.contains(value)) {
                numbers.add(value);
            }
        }

        return numbers;
    }

    private List<Integer> generateUniqueRandomNumbers(int upperBound) {
        return generateUniqueRandomNumbers(upperBound, NUMBER_OF_OPTIONS);
    }
}
