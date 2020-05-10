package cagreer.password_generator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SchneierGeneratorMethod implements GeneratorMethod {
	public static final int MIN_OFFSET = 1;

	public final int preferedOffset;
	public final int minCharacters;

	public SchneierGeneratorMethod(int preferedOffset, int minCharacters) {
		if (preferedOffset < MIN_OFFSET) {
			throw new IllegalArgumentException(
					String.format("PreferedOffset :: minimum: %d , actual: %d", MIN_OFFSET, preferedOffset));
		}

		if (minCharacters < MIN_CHARACTERS) {
			throw new IllegalArgumentException(
					String.format("MinCharacters :: minimum: %d , actual: %d", MIN_CHARACTERS, minCharacters));
		}

		this.preferedOffset = preferedOffset;
		this.minCharacters = minCharacters;
	}

	public SchneierGeneratorMethod(int preferedOffset) {
		this(preferedOffset, MIN_CHARACTERS);
	}

	public SchneierGeneratorMethod() {
		this(MIN_OFFSET, MIN_CHARACTERS);
	}

	@Override
	public String toString() {
		return getClass().getName() + ": [preferedOffset=" + preferedOffset + ", minCharacters=" + minCharacters + "]";
	}

	@Override
	public String generateFrom(String input) {
		if(input == null) {
			throw new NullPointerException();
		}
		
		String[] words = input.split("\\s");
		Iterable<String> itr = Arrays.asList(words);
		validateInput(itr);

		return createPassword(itr);
	}

	private void validateInput(Iterable<String> itr) {
		int maxCharacters = getTotalLength(
				Pattern.compile("\\b\\w{1,}|(" + GeneratorMethod.SPECIAL_CHARACTER_SET + "+(?![a-zA-Z0-9]))"), itr);

		if (maxCharacters < minCharacters) {
			throw new IllegalArgumentException();
		}
	}

	private static int getTotalLength(Pattern p, Iterable<String> itr) {
		int totalCharacters = 0;

		for (String s : itr) {
			Matcher m = p.matcher(s);

			while (m.find()) {
				totalCharacters += m.group().length();
			}
		}

		return totalCharacters;
	}

	private String createPassword(Iterable<String> itr) {
		int adjustedOffset = preferedOffset;
		StringBuilder sb = new StringBuilder();

		do {
			sb.setLength(0);
			Pattern p = Pattern.compile(
					"\\b\\w{1," + adjustedOffset + "}|(" + GeneratorMethod.SPECIAL_CHARACTER_SET + "+(?![a-zA-Z0-9]))");

			for (String str : itr) {
				Matcher m = p.matcher(str);

				while (m.find()) {
					sb.append(m.group());
				}
			}
			adjustedOffset++;
		} while (sb.toString().length() < minCharacters);

		return sb.toString();
	}

}
