package br.com.gabrielferreira.utils.validation;

public class CpfValidation {

	// CPF
	private static final int[] WEIGHT_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static int recursiveSum(int[] weight, char[] chr, int number) {
		if (number <= 0)
			return 0;
		final int chrIndex = number - 1;
		final int weightIndex = weight.length > chr.length ? number : chrIndex;
		return (recursiveSum(weight, chr, chrIndex) + Character.getNumericValue(chr[chrIndex]) * weight[weightIndex]);
	}

	private static int calculate(final String str, final int[] weight) {
		final char[] chr = str.toCharArray();
		int sum = recursiveSum(weight, chr, chr.length);
		sum = 11 - (sum % 11);
		return sum > 9 ? 0 : sum;
	}

	private static boolean checkEquals(String cnpj, int length, int[] weight) {
		final String number = cnpj.substring(0, length);
		final int digit1 = calculate(number, weight);
		final int digit2 = calculate(number + digit1, weight);
		return cnpj.equals(number + digit1 + digit2);
	}

	/**
	 * Valida CPF
	 *
	 * @param cpf
	 * @return
	 */
	public static boolean isValidCPF(String cpf) {
		if (cpf == null || !cpf.matches("\\d{11}") || cpf.matches(cpf.charAt(0) + "{11}"))
			return false;
		return checkEquals(cpf, 9, WEIGHT_CPF);
	}
	
}
