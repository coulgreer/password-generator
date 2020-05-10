package cagreer.password_generator;

public interface GeneratorMethod {
	public static final int MIN_CHARACTERS = 12;
	public static final String SPECIAL_CHARACTER_SET = "[ !\\\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]";
	// The same list as string (between double quotes):
	// " !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~"

	public String generateFrom(String input);

}
