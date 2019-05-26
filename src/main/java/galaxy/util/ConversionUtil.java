package galaxy.util;

import galaxy.Expr;
import galaxy.roman.RomanToNumberUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Conversion utils.
 */
public final class ConversionUtil {

    /**
     * Intergalatic unit to int
     */
    public static int intergalacticUnitToInt(List<Expr> right, Map<String, String> context) {
        return RomanToNumberUtil.convert(right.stream().map(e -> e.interpret(context)).collect(Collectors.joining()));
    }

    /**
     * Intergalatic unit to decimal
     */
    public static int intergalacticUnitToDecimal(List<String> words, Map<String, String> context) {
        String roman = words.stream().map(s -> Expr.variable(s).interpret(context)).collect(Collectors.joining());
        return RomanToNumberUtil.convert(roman);
    }

    private ConversionUtil() {
    }
}
