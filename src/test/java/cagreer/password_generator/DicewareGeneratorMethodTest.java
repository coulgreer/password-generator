package cagreer.password_generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public final class DicewareGeneratorMethodTest {

	@Test(expected = IllegalArgumentException.class)
	public void DicewareGeneratorMethod_WordCountLessThanMinWordCount_ThrowsException() throws IOException {
		String wordListPath = DicewareGeneratorMethod.ORIGINAL_WORD_LIST_PATH;
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT - 1;
		boolean hasExtraSecurity = false;

		@SuppressWarnings("unused")
		DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
	}

	@Test(expected = NullPointerException.class)
	public void DicewareGeneratorMethod_WordListPathIsNull_ThrowsException() throws IOException {
		String wordListPath = null;
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT;
		boolean hasExtraSecurity = false;

		@SuppressWarnings("unused")
		DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
	}

	@Test(expected = IOException.class)
	public void DicewareGeneratorMethod_InvalidPath_ThrowsException() throws IOException {
		String wordListPath = "A File Path That Should Not Work";
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT;
		boolean hasExtraSecurity = false;

		@SuppressWarnings("unused")
		DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
	}

	@Test(expected = IllegalArgumentException.class)
	public void generateKeyString_WordCountLessThanMinWordCount_ExceptionThrown() {
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT - 1;

		DicewareGeneratorMethod.generateKeyString(wordCount);
	}

	@Test
	public void generateKeyString_ValidWordCount_FormattedString() {
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT;

		String result = DicewareGeneratorMethod.generateKeyString(wordCount);
		String[] resultArray = result.split("\\s+");

		boolean hasEnoughKeys = resultArray.length == wordCount;
		boolean hasInvalidKeyLength = false;

		for (String str : resultArray) {
			if (str.length() != DicewareGeneratorMethod.KEY_LENGTH) {
				hasInvalidKeyLength = true;
				break;
			}
		}

		assertTrue(hasEnoughKeys && !hasInvalidKeyLength);
	}

	@Test(expected = NullPointerException.class)
	public void generateFrom_NullInput_ThrowsException() {
		String wordListPath = DicewareGeneratorMethod.ORIGINAL_WORD_LIST_PATH;
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT;
		boolean hasExtraSecurity = false;

		try {
			DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
			dgm.generateFrom(null);
		} catch (IOException ioExc) {
			Assert.fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void generateFrom_WordCountOfInputLessThanTargetWordCount_ThrowsException() {
		String wordListPath = DicewareGeneratorMethod.ORIGINAL_WORD_LIST_PATH;
		int wordCount = 6;
		boolean hasExtraSecurity = false;
		String invalidInput = buildInput( //
				DicewareGeneratorMethod.MIN_WORD_COUNT - 1, //
				DicewareGeneratorMethod.LOWER_BOUND);

		try {
			DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
			dgm.generateFrom(invalidInput);
		} catch (IOException ioExc) {
			Assert.fail();
		}
	}

	private static String buildInput(int wordCount, int roll) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < wordCount; i++) {
			sb.append(roll + " ");
		}
		return sb.toString().trim();
	}

	@Test(expected = IllegalArgumentException.class)
	public void generateFrom_WordLengthLessThanTargetLength_ThrowsException() {
		String wordListPath = DicewareGeneratorMethod.ORIGINAL_WORD_LIST_PATH;
		int wordCount = DicewareGeneratorMethod.MIN_WORD_COUNT;
		boolean hasExtraSecurity = false;
		String invalidInput = buildInvalidInput(wordCount);

		try {
			DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
			dgm.generateFrom(invalidInput);
		} catch (IOException ioExc) {
			Assert.fail();
		}
	}

	private static String buildInvalidInput(int wordCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < wordCount; i++) {
			sb.append("1 ");
		}
		return sb.toString().trim();
	}

	@Test
	public void generateFrom_InputThatConvertsIntoAPassphraseGreaterThanMinCharacters_FormattedOutput() {
		String wordListPath = DicewareGeneratorMethod.ORIGINAL_WORD_LIST_PATH;
		int wordCount = 4;
		boolean hasExtraSecurity = false;
		String input = buildInput(wordCount, 11121);

		try {
			DicewareGeneratorMethod dgm = new DicewareGeneratorMethod(wordListPath, wordCount, hasExtraSecurity);
			String actual = dgm.generateFrom(input);
			String expected = "aaronaaronaaronaaron";

			assertEquals(expected, actual);
		} catch (IOException ioExc) {
			Assert.fail();
		}
	}

}
