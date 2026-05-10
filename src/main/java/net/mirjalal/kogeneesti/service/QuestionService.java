package net.mirjalal.kogeneesti.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.mirjalal.kogeneesti.model.dto.QuestionDto;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.Word;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final static int NUMBER_OF_OPTIONS = 4;

    private final Random random = new Random();
    private final DictionaryService dictionaryService;

    public QuestionDto generateQuestion(BigInteger dictionaryId) {
        List<Word> words = this.getWordsByDictionaryId(dictionaryId);

        List<Integer> numbers = this.generateUniqueRandomNumbers(words.size());
        Iterator<Integer> iterator = numbers.iterator();
        
        Word correctWord = words.get(iterator.next());
        List<String> options = new ArrayList<>();
        while (iterator.hasNext()) {
            options.add(words.get(iterator.next()).getTranslation());
        }
        options.add(correctWord.getTranslation());

        QuestionDto questionDto = new QuestionDto(
            correctWord.getWord(),
            correctWord.getTranslation(),
            options
        );

        return questionDto;
    }

    public QuestionDto generateReversedQuestion(BigInteger dictionaryId) {
        List<Word> words = this.getWordsByDictionaryId(dictionaryId);

        List<Integer> numbers = this.generateUniqueRandomNumbers(words.size());
        Iterator<Integer> iterator = numbers.iterator();
        
        Word correctWord = words.get(iterator.next());
        List<String> options = new ArrayList<>();
        while (iterator.hasNext()) {
            options.add(words.get(iterator.next()).getWord());
        }
        options.add(correctWord.getWord());
        QuestionDto questionDto = new QuestionDto(
            correctWord.getTranslation(),
            correctWord.getWord(),
            options
        );

        return questionDto;
    }

    private List<Word> getWordsByDictionaryId(BigInteger dictionaryId) {
        Dictionary dictionary = dictionaryService.getDictionary(dictionaryId);

        List<Word> words = dictionary.getWords();
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
