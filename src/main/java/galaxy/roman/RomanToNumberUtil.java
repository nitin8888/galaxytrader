package galaxy.roman;

import java.util.stream.IntStream;

/**
 * Roman to Number convert functionality
 */
public final class RomanToNumberUtil {

    /**
     * Regex for valid Roman String
     */
    private static final String VALID_ROMAN = "^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

    /**
     * Converts Roman value to number
     * @param roman the roman value
     * @return the converted number
     */
    public static int convert(String roman) {
        validateRoman(roman);
        return IntStream.range(0, roman.toCharArray().length).map(i -> romanToInt(roman.toCharArray(), i)).sum();
    }

    private static void validateRoman(String roman) {
        if (!roman.matches(VALID_ROMAN)) {
            throw new IllegalArgumentException("Invalid Roman value:" + roman);
        }
    }

    private static int romanToInt(char[] romanChars, int i) {
        Roman current = getRoman(romanChars[i]);
        // if current > prev return 0
        if (i > 0 && current.ordinal() > getRoman(romanChars[i-1]).ordinal()) {
            return 0;
        } else if (i < romanChars.length-1) {
            // if current < next return nextValue - currentValue
            Roman next = getRoman(romanChars[i+1]);
            if (current.ordinal() < next.ordinal()) {
                return next.getValue() - current.getValue();
            }
        }
        return current.getValue();
    }

    private static Roman getRoman(char aChar) {
        return Roman.valueOf(String.valueOf(aChar));
    }

    private RomanToNumberUtil() {
    }
}
