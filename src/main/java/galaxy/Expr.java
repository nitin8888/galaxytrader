package galaxy;

import galaxy.roman.RomanToNumberUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static galaxy.common.CommonText.CREDITS;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Expr interface for Interpreter design pattern with a lambda for each grammar rule
 */
@FunctionalInterface
public interface Expr {

    /**
     * Method to interpret Expr
     * @param context the context
     * @return the interpreted text
     */
    String interpret(Map<String, String> context);

    /**
     * How much is question rule
     */
    static Expr howMuchIs(Expr left, List<Expr> right) {
        return context -> left.interpret(context) + SPACE + intergalacticUnitToInt(right, context);
    }

    /**
     * How many credit is question rule
     */
    static Expr howManyCreditIs(Expr left, List<Expr> right, BigDecimal metalSingleUnit) {
        return context -> {
            BigDecimal romanToDecimal = BigDecimal.valueOf(intergalacticUnitToInt(right, context)).multiply(metalSingleUnit);
            return left.interpret(context) + SPACE + String.format("%.0f", romanToDecimal) + SPACE + CREDITS;
        };
    }

    /**
     * Variable rule
     */
    static Expr variable(String name) {
        return context -> context.getOrDefault(name, "");
    }

    static int intergalacticUnitToInt(List<Expr> right, Map<String, String> context) {
        return RomanToNumberUtil.convert(right.stream().map(e -> e.interpret(context)).collect(Collectors.joining()));
    }
}
