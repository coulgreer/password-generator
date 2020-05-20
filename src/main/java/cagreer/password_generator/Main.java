package cagreer.password_generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main {
	private static final String INDENT = "   ";

	private static int indentLevel = 0;

	private Main() {
	}

	public static void main(String[] args) {

		System.out.println("Choose which generation method to use:\n");

		indentLevel++;
		printMethodDescription();
		System.out.print("\n" + //
				createIndent() + "(1) Schneier\n" + //
				createIndent() + "--- Diceware ---\n" + //
				createIndent() + "(2) Original Diceware\n" + //
				createIndent() + "(3) Beale Diceware\n" + //
				createIndent() + "(4) EFF Long\n");

		try (Scanner sc = new Scanner(System.in)) {
			System.out.print(createIndent() + "Choose One: ");
			String methodChoice = sc.nextLine();

			while (!methodChoice.matches("1|2|3|4")) {
				System.out.println(createIndent() + "Please choose a valid input: '1', '2', '3', '4");

				indentLevel++;
				System.out.print(createIndent() + "Input: ");
				methodChoice = sc.nextLine();
				indentLevel--;
			}

			System.out.print("\n\n");

			if (methodChoice.equals("1")) {
				System.out.println(createIndent() + "You chose the Schneier Method!");
				indentLevel++;
				buildSchneierPassphrase(sc);
				indentLevel--;
			} else if (methodChoice.equals("2")) {
				System.out.println(createIndent() + "You chose the ORIGINAL Diceware Method!");
				indentLevel++;
				buildDicewarePassphrase(sc, DicewareGeneratorMethod.ORIGINAL_WORD_LIST_PATH);
				indentLevel--;
			} else if (methodChoice.equals("3")) {
				System.out.println(createIndent() + "You chose the BEALE Diceware Method!");
				indentLevel++;
				buildDicewarePassphrase(sc, DicewareGeneratorMethod.BEALE_WORD_LIST_PATH);
				indentLevel--;
			} else if (methodChoice.equals("4")) {
				System.out.println(createIndent() + "You chose the EFF Long Method!");
				indentLevel++;
				buildDicewarePassphrase(sc, DicewareGeneratorMethod.EFF_WORD_LIST_PATH);
				indentLevel--;
			}
		}

		indentLevel--;
	}

	private static void printMethodDescription() {
		int maxLineLength = 100;

		String schneierTitle = "SCHNEIER Method";
		String schneierRawText = schneierTitle + ": " + SchneierGeneratorMethod.DESCRIPTION;
		System.out.println(formatTextForConsole(maxLineLength, schneierRawText) + "\n\n");

		String dicewareTitle = "DICEWARE Method";
		String dicewareRawText = dicewareTitle + ": " + DicewareGeneratorMethod.DESCRIPTION;
		System.out.println(formatTextForConsole(maxLineLength, dicewareRawText) + "\n\n");

	}

	private static String formatTextForConsole(int maxLineLength, String rawText) {
		Pattern p = Pattern.compile("\\s+|((?<=[0-9a-zA-Z]+)-(?=[0-9a-zA-Z]+))");
		String formattedText = rawText.substring(0, maxLineLength);
		rawText = rawText.substring(maxLineLength);
		StringBuilder sb = new StringBuilder();
		sb.append(formattedText);
		do {
			if (rawText.length() > maxLineLength - INDENT.length()) {
				formattedText = rawText.substring(0, maxLineLength - INDENT.length());
				Matcher m = p.matcher(formattedText);
				String lastMatch = "";
				while (m.find()) {
					lastMatch = m.group();
				}
				int lastIndex = formattedText.lastIndexOf(lastMatch);
				formattedText = formattedText.substring(0, lastIndex).trim();
				rawText = rawText.substring(lastIndex);
			} else {
				formattedText = rawText.trim();
				rawText = "";
			}
			sb.append("\n" + INDENT + formattedText);
		} while (rawText.length() > 0);
		formattedText = sb.toString();

		return formattedText;
	}

	private static void buildSchneierPassphrase(Scanner sc) {
		SchneierGeneratorMethod schneier;

		int preferedOffset = promptPreferedOffset(sc);
		System.out.println();
		int minCharacters = promptMinCharacters(sc);
		schneier = new SchneierGeneratorMethod(preferedOffset, minCharacters);
		System.out.println();
		String passphrase = schneier.generateFrom(promptSentance(sc, minCharacters));

		indentLevel = 0;
		System.out.println("\n\n" + createIndent() + "Your generated passphrase is:");
		System.out.println(createIndent() + passphrase);
	}

	private static int promptPreferedOffset(Scanner sc) {
		System.out.println(createIndent() + "Please choose an offset for each word. A minimum value of "
				+ SchneierGeneratorMethod.MIN_OFFSET + ".");

		indentLevel++;
		System.out.print(createIndent() + "Input: ");
		String input = sc.nextLine();
		indentLevel--;

		return parseToInt(sc, input, SchneierGeneratorMethod.MIN_OFFSET);
	}

	private static int promptMinCharacters(Scanner sc) {
		System.out.println(createIndent() + "Please choose the minimum characters needed. A minimum value of "
				+ GeneratorMethod.MIN_CHARACTERS + ".");

		indentLevel++;
		System.out.print(createIndent() + "Input: ");
		String input = sc.nextLine();
		indentLevel--;

		return parseToInt(sc, input, GeneratorMethod.MIN_CHARACTERS);
	}

	private static String promptSentance(Scanner sc, int minCharacters) {
		System.out.println(createIndent() + "Finally, put in a sentance to be turned into your passphrase.");

		indentLevel++;
		System.out.print(createIndent() + "Input: ");
		String sentance = sc.nextLine();
		indentLevel--;

		return validateSentance(sc, sentance, minCharacters);
	}

	private static String validateSentance(Scanner sc, String input, int minCharacters) {
		Iterable<String> itr = Arrays.asList(input.split("\\s+"));
		int maxCharacters = getTotalLength(
				Pattern.compile("\\b\\w{1,}|(" + GeneratorMethod.SPECIAL_CHARACTER_SET + "+(?![a-zA-Z0-9]))"), itr);

		while (maxCharacters < minCharacters) {
			System.out.println(createIndent()
					+ "Please enter a valid sentance. All characters -- excluding SPACE -- should equal to "
					+ minCharacters + " or greater.");

			indentLevel++;
			System.out.print(createIndent() + "Input: ");
			input = sc.nextLine();
			indentLevel--;

			input = validateSentance(sc, input, minCharacters);
			itr = Arrays.asList(input.split("\\s+"));
			maxCharacters = getTotalLength(
					Pattern.compile("\\b\\w{1,}|(" + GeneratorMethod.SPECIAL_CHARACTER_SET + "+(?![a-zA-Z0-9]))"), itr);
		}

		return input;
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

	private static void buildDicewarePassphrase(Scanner sc, String listPath) {
		DicewareGeneratorMethod diceware;

		int wordCount = promptWordCount(sc);
		System.out.println();
		boolean hasExtraSecurity = promptExtraSecurity(sc);

		try {
			diceware = new DicewareGeneratorMethod( //
					listPath, //
					wordCount, //
					hasExtraSecurity);
			String key = DicewareGeneratorMethod.generateKeyString(wordCount);
			String passphrase = diceware.generateFrom(key);

			indentLevel = 0;
			System.out.println("\n\n" + createIndent() + "Your generated passphrase is:");
			System.out.println(createIndent() + passphrase);
		} catch (IOException ioExc) {
			ioExc.printStackTrace();
		}
	}

	private static int promptWordCount(Scanner sc) {
		System.out.println(createIndent() + "Please choose the amount of words for the passphrase. A minimum value of "
				+ DicewareGeneratorMethod.MIN_WORD_COUNT + ".");

		indentLevel++;
		System.out.print(createIndent() + "Input: ");
		String input = sc.nextLine();
		indentLevel--;

		return parseToInt(sc, input, DicewareGeneratorMethod.MIN_WORD_COUNT);

	}

	private static boolean promptExtraSecurity(Scanner sc) {
		System.out.println(createIndent() + "Do you need extra security?");

		indentLevel++;
		System.out.print(createIndent() + "Input: ");
		String input = sc.nextLine();
		indentLevel--;

		return parseToBoolean(sc, input);
	}

	private static String createIndent() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < indentLevel; i++) {
			sb.append(INDENT);
		}

		return sb.toString();
	}

	private static int parseToInt(Scanner sc, String input, int lowerBound) {
		int result = 0;

		while (!input.matches("[0-9]+")) {
			System.out.println(createIndent() + "Please enter a number. ");

			indentLevel++;
			System.out.print(createIndent() + "Input: ");
			input = sc.nextLine();
			indentLevel--;

			input = Integer.toString(parseToInt(sc, input, lowerBound));
		}

		result = Integer.parseInt(input);

		while (result < lowerBound) {
			System.out.println(
					createIndent() + "The number must be greater than or equal to " + lowerBound + ". Try again! ");

			indentLevel++;
			System.out.print(createIndent() + "Input: ");
			input = sc.nextLine();
			indentLevel--;

			result = parseToInt(sc, input, lowerBound);
		}

		return result;
	}

	private static boolean parseToBoolean(Scanner sc, String input) {
		boolean result = false;

		while (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
			System.out.println(createIndent() + "Please select 'true' or 'false'");

			indentLevel++;
			System.out.print(createIndent() + "Input: ");
			input = sc.nextLine();
			indentLevel--;

			result = parseToBoolean(sc, input);
		}

		result = Boolean.parseBoolean(input);

		return result;
	}

}
