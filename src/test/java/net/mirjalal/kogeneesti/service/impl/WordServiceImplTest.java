package net.mirjalal.kogeneesti.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.mirjalal.kogeneesti.exception.NotFoundException;
import net.mirjalal.kogeneesti.model.Dictionary;
import net.mirjalal.kogeneesti.model.Word;
import net.mirjalal.kogeneesti.model.dto.word.WordCreateRequestDto;
import net.mirjalal.kogeneesti.model.dto.word.WordGetResponseDto;
import net.mirjalal.kogeneesti.model.mapper.WordMapper;
import net.mirjalal.kogeneesti.repository.WordRepository;
import net.mirjalal.kogeneesti.service.DictionaryService;

@ExtendWith(MockitoExtension.class)
class WordServiceImplTest {

	@Mock
	private WordRepository wordRepository;

	@Mock
	private WordMapper wordMapper;

	@Mock
	private DictionaryService dictionaryService;

	@InjectMocks
	private WordServiceImpl wordService;

	@Test
	void createWordShouldPersistAndReturnDto() {
		BigInteger dictionaryId = BigInteger.ONE;
		WordCreateRequestDto request = new WordCreateRequestDto("cat", "kass");
		Dictionary dictionary = new Dictionary();
		dictionary.setId(dictionaryId);
		Word word = new Word();
		word.setWord("cat");
		word.setTranslation("kass");
		WordGetResponseDto expected = new WordGetResponseDto(BigInteger.TEN, "cat", "kass", dictionaryId);

		when(wordMapper.toEntity(request)).thenReturn(word);
		when(dictionaryService.getDictionaryEntity(dictionaryId)).thenReturn(dictionary);
		when(wordMapper.toGetResponseDto(word)).thenReturn(expected);

		WordGetResponseDto actual = wordService.createWord(request, dictionaryId);

		assertEquals(expected, actual);
		assertEquals(dictionary, word.getDictionary());
		verify(wordRepository).save(word);
	}

	@Test
	void getWordShouldReturnMappedDto() {
		BigInteger id = BigInteger.valueOf(5);
		Word existingWord = existingWord(id, BigInteger.TWO, "dog", "koer");
		WordGetResponseDto expected = new WordGetResponseDto(id, "dog", "koer", BigInteger.TWO);

		when(wordRepository.findById(id)).thenReturn(Optional.of(existingWord));
		when(wordMapper.toGetResponseDto(existingWord)).thenReturn(expected);

		WordGetResponseDto actual = wordService.getWord(id);

		assertEquals(expected, actual);
	}

	@Test
	void getWordShouldThrowWhenMissing() {
		BigInteger id = BigInteger.valueOf(99);
		when(wordRepository.findById(id)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> wordService.getWord(id));

		assertEquals("Word not found", exception.getMessage());
	}

	@Test
	void editWordShouldPreserveIdAndDictionaryAndReturnDto() {
		BigInteger id = BigInteger.valueOf(7);
		BigInteger dictionaryId = BigInteger.valueOf(3);
		Word existingWord = existingWord(id, dictionaryId, "bird", "lind");
		WordCreateRequestDto request = new WordCreateRequestDto("water", "vesi");
		Word mappedUpdatedWord = new Word();
		mappedUpdatedWord.setWord("water");
		mappedUpdatedWord.setTranslation("vesi");
		WordGetResponseDto expected = new WordGetResponseDto(id, "water", "vesi", dictionaryId);

		when(wordRepository.findById(id)).thenReturn(Optional.of(existingWord));
		when(wordMapper.toEntity(request)).thenReturn(mappedUpdatedWord);
		when(wordMapper.toGetResponseDto(mappedUpdatedWord)).thenReturn(expected);

		WordGetResponseDto actual = wordService.editWord(id, request);

		assertEquals(expected, actual);
		assertEquals(id, mappedUpdatedWord.getId());
		assertEquals(existingWord.getDictionary(), mappedUpdatedWord.getDictionary());
		verify(wordRepository).save(mappedUpdatedWord);
	}

	@Test
	void editWordShouldThrowWhenWordMissing() {
		BigInteger id = BigInteger.valueOf(88);
		when(wordRepository.findById(id)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> wordService.editWord(id, new WordCreateRequestDto("cat", "kass")));

		assertEquals("Word not found", exception.getMessage());
	}

	@Test
	void deleteWordShouldRemoveExistingWord() {
		BigInteger id = BigInteger.valueOf(11);
		Word existingWord = existingWord(id, BigInteger.ONE, "house", "maja");
		when(wordRepository.findById(id)).thenReturn(Optional.of(existingWord));

		wordService.deleteWord(id);

		verify(wordRepository).delete(existingWord);
	}

	@Test
	void deleteWordShouldThrowWhenWordMissing() {
		BigInteger id = BigInteger.valueOf(12);
		when(wordRepository.findById(id)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> wordService.deleteWord(id));

		assertEquals("Word not found", exception.getMessage());
	}

	private Word existingWord(BigInteger wordId, BigInteger dictionaryId, String wordText, String translationText) {
		Dictionary dictionary = new Dictionary();
		dictionary.setId(dictionaryId);

		Word word = new Word();
		word.setId(wordId);
		word.setWord(wordText);
		word.setTranslation(translationText);
		word.setDictionary(dictionary);

		return word;
	}
}
