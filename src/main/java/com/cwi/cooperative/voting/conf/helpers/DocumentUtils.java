package com.cwi.cooperative.voting.conf.helpers;


/**
 * Credit https://github.com/adrianoluis/misc-tools.git
 * @author Adriano Rocha
 * @license MIT License
 */
public class DocumentUtils {	
	// CPF
	private static final int[] WEIGHT_SSN = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	
	// CNPJ
	private static final int[] WEIGHT_TFN = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	
	private static int sum(int[] weight, char[] numbers, int length) {
		if (length <= 0) return 0;
		final int nIdx = length - 1;
		final int wIdx = weight.length > numbers.length ? length : nIdx;
		return (sum(weight, numbers, nIdx) + Character.getNumericValue(numbers[nIdx]) * weight[wIdx]);
	}
	
	private static int calculate(final String document, final int[] weight) {
		final char[] numbers = document.toCharArray();
		int sum = sum(weight, numbers, numbers.length);
		sum = 11 - (sum % 11);
		return sum > 9 ? 0 : sum;
	}
	
	private static boolean check(String tfn, int length, int[] weight) {
		final String number = tfn.substring(0, length);
		final int digit1 = calculate(number, weight);
		final int digit2 = calculate(number + digit1, weight);
		return tfn.equals(number + digit1 + digit2);
	}
	
	/**
	 * Valida CPF
	 */
	public static boolean isValidCPF(String cpf) {
		if (cpf == null || !cpf.matches("\\d{11}") || cpf.matches(cpf.charAt(0) + "{11}")) return false;
		return check(cpf, 9, WEIGHT_SSN);
	}
	
	/**
	 * Valida CNPJ
	 */
	public static boolean isValidCNPJ(String cnpj) {
		if (cnpj == null || !cnpj.matches("\\d{14}")) return false;
		return check(cnpj, 12, WEIGHT_TFN);
	}

}
