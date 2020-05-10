package cagreer.password_generator;

import org.junit.Assert;
import org.junit.Test;

public class SchneierGeneratorMethodTest {

	@Test(expected = IllegalArgumentException.class)
	public void SchneierGeneratorMethod_MinCharactersArgumentLessThanMinCharactersConstant_ThrowsException() {
		int offset = SchneierGeneratorMethod.MIN_OFFSET;
		int minCharacters = GeneratorMethod.MIN_CHARACTERS - 1;

		@SuppressWarnings("unused")
		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
	}

	@Test(expected = IllegalArgumentException.class)
	public void SchneierGeneratorMethod_MinOffsetArgumentLessThanMinOffsetConstant_ThrowsException() {
		int offset = SchneierGeneratorMethod.MIN_OFFSET - 1;
		int minCharacters = GeneratorMethod.MIN_CHARACTERS;

		@SuppressWarnings("unused")
		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
	}

	@Test(expected = IllegalArgumentException.class)
	public void generateFrom_InputLessThanMinCharacters_ExceptionThrown() {
		String input = "Less than 12";
		int offset = input.length();
		int minCharacters = 12;

		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
		sgm.generateFrom(input);
	}

	@Test
	public void generateFrom_ValidInput_ReformattedInput() {
		int offset = 3;
		int minCharacters = GeneratorMethod.MIN_CHARACTERS;
		String input = "This input perfectly fits with the offset.";
		String expected = "Thiinpperfitwittheoff.";

		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
		String actual = sgm.generateFrom(input);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void generateFrom_ValidInputWithWordsShorterThanOffset_ReformattedInput() {
		int offset = 5;
		int minCharacters = GeneratorMethod.MIN_CHARACTERS;
		String input = "This input is FULL of words less than the offset.";
		String expected = "ThisinputisFULLofwordslessthantheoffse.";

		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
		String actual = sgm.generateFrom(input);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void generateFrom_ValidInputWithContinuousSequenceOfSpecialCharacters_ReformattedInput() {
		int offset = 3;
		int minCharacters = GeneratorMethod.MIN_CHARACTERS;
		String input = "This input -- pause for effect -- has continuous sequences of special characters!?";
		String expected = "Thiinp--pauforeff--hasconseqofspecha!?";

		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
		String actual = sgm.generateFrom(input);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void generateFrom_ValidInputWithTooSmallOffset_ReformattedInput() {
		int offset = 1;
		int minCharacters = 12;
		String input = "This sentance has less than 12 words in it.";
		String expected = "Thsehaleth12woinit.";

		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod(offset, minCharacters);
		String actual = sgm.generateFrom(input);

		Assert.assertEquals(expected, actual);
	}
	
	@Test(expected = NullPointerException.class) 
	public void generateFrom_NullInput_ThrowsException() {
		SchneierGeneratorMethod sgm = new SchneierGeneratorMethod();
		sgm.generateFrom(null);
	}

}
