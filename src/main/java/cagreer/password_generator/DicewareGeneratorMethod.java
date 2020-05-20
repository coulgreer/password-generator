package cagreer.password_generator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class DicewareGeneratorMethod implements GeneratorMethod {
	public static final String ORIGINAL_WORD_LIST_PATH = "./resources/word-lists/original-wordlist.txt";
	public static final String BEALE_WORD_LIST_PATH = "./resources/word-lists/beale-wordlist.txt";
	public static final String EFF_WORD_LIST_PATH = "./resources/word-lists/eff-large-wordlist.txt";

	public static final String DESCRIPTION = "A method for picking passphrases that uses ordinary dice "
			+ "to select words at random from a special list. Each word in the list is preceded by a "
			+ "five digit number. All the digits are between one and six, allowing you to use the "
			+ "outcomes of five dice rolls to select a word from the list.\n\n   Quoted from: "
			+ "https://theworld.com/~reinhold/diceware.html";

	public static final int MIN_WORD_COUNT = 4;
	public static final int KEY_LENGTH = 5;

	public static final int LOWER_BOUND = 1;
	public static final int UPPER_BOUND = 6;

	private static final char[][] REPLACEMENT_CHARACTERS = { //
			{ '~', '!', '#', '$', '%', '^' }, //
			{ '&', '*', '(', ')', '-', '=' }, //
			{ '+', '[', ']', '\\', '{', '}' }, //
			{ ':', ';', '\"', '\'', '<', '>' }, //
			{ '?', '/', '0', '1', '2', '3' }, //
			{ '4', '5', '6', '7', '8', '9' } //
	};
	private static final int D6_FACES = 6;

	private final Map<Integer, String> wordsByDiceRoll;
	private final int wordCount;
	private final boolean hasExtraSecurity;

	public DicewareGeneratorMethod(String wordListPath, int wordCount, boolean hasExtraSecurity) throws IOException {
		Path wordList = Paths.get(wordListPath);
		try (Scanner sc = new Scanner(wordList)) {
			wordsByDiceRoll = parseWordList(sc);
		}

		if (wordCount < MIN_WORD_COUNT) {
			throw new IllegalArgumentException( //
					String.format("WordCount :: minimum: %d , actual: %d", //
							MIN_WORD_COUNT, //
							wordCount));
		}

		this.wordCount = wordCount;
		this.hasExtraSecurity = hasExtraSecurity;
	}

	private static Map<Integer, String> parseWordList(Scanner sc) {
		HashMap<Integer, String> wordsByDiceRoll = new HashMap<>();

		while (sc.hasNextLine()) {
			String[] pair = sc.nextLine().split("\\s+");
			Integer key = Integer.parseInt(pair[0]);
			String value = pair[1];
			wordsByDiceRoll.put(key, value);
		}

		return wordsByDiceRoll;
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " //
				+ "[wordsByDiceRoll=" + wordsByDiceRoll + ", " //
				+ "wordCount=" + wordCount + ", " //
				+ "hasExtraSecurity=" + hasExtraSecurity + "]";
	}

	@Override
	public String generateFrom(String input) {
		StringBuilder sb = new StringBuilder();
		String[] keys = createKeysFrom(input);

		do {
			String[] words = createWordsFrom(keys);
			sb.setLength(0);

			if (hasExtraSecurity) {
				replaceCharacter(words);
			}

			for (String word : words) {
				sb.append(word);
			}

			keys = createKeysFrom(generateKeyString(wordCount));
		} while (sb.toString().length() < GeneratorMethod.MIN_CHARACTERS);

		return sb.toString();
	}

	private String[] createKeysFrom(String input) {
		String[] keys = input.split("\\s+");
		validateKeys(keys);
		return keys;
	}

	private void validateKeys(String[] keys) {
		boolean hasEnoughKeys = keys.length == wordCount;
		if (!hasEnoughKeys) {
			throw new IllegalArgumentException( //
					String.format("Number of words is: %d , but should be %d", //
							keys.length, //
							wordCount));
		}

		for (String key : keys) {
			validateKeyMember(key);
		}
	}

	private static void validateKeyMember(String key) {
		if (key.length() != DicewareGeneratorMethod.KEY_LENGTH) {
			throw new IllegalArgumentException( //
					String.format("The key: %s is invalid. Key length: %d, but required length %d", //
							key, //
							key.length(), //
							DicewareGeneratorMethod.KEY_LENGTH));
		}
	}

	private String[] createWordsFrom(String[] keys) {
		String[] words = new String[wordCount];
		for (int i = 0; i < wordCount; i++) {
			words[i] = wordsByDiceRoll.get(Integer.parseInt(keys[i]));
		}
		return words;
	}

	private static void replaceCharacter(String[] words) {
		SecureRandom secRandom = new SecureRandom();

		int firstRoll = secRandom.nextInt(D6_FACES);
		while (firstRoll > words.length) {
			firstRoll -= words.length;
		}

		int secondRoll = secRandom.nextInt(D6_FACES);
		String word = words[firstRoll];
		while (secondRoll > word.length()) {
			secondRoll -= word.length();
		}

		words[firstRoll] = secondRoll < word.length()
				? word.substring(0, secondRoll)
						+ REPLACEMENT_CHARACTERS[secRandom.nextInt(D6_FACES)][secRandom.nextInt(D6_FACES)]
						+ word.substring(secondRoll + 1)
				: word.substring(0, secondRoll)
						+ REPLACEMENT_CHARACTERS[secRandom.nextInt(D6_FACES)][secRandom.nextInt(D6_FACES)];
	}

	public static String generateKeyString(int wordCount) {
		if (wordCount < MIN_WORD_COUNT) {
			throw new IllegalArgumentException( //
					String.format("WordCount :: minimum: %d , actual: %d", //
							MIN_WORD_COUNT, //
							wordCount));
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < wordCount; i++) {
			sb.append(rollDie() + rollDie() + rollDie() + rollDie() + rollDie() + " ");
		}

		return sb.toString().trim();
	}

	private static String rollDie() {
		SecureRandom secRandom = new SecureRandom();
		int value = LOWER_BOUND + secRandom.nextInt(UPPER_BOUND);
		return Integer.toString(value);
	}

}
