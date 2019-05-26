package galaxy.questions;

import galaxy.Expr;

import java.math.BigDecimal;
import java.util.List;

import static galaxy.common.CommonText.CREDITS;
import static galaxy.util.ConversionUtil.intergalacticUnitToInt;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Hom many credit is question
 */
public class HowManyCreditIs {

    /**
     * How many credit is question rule
     */
    public static Expr question(Expr left, List<Expr> right, BigDecimal metalSingleUnit) {
        return context -> {
            BigDecimal romanToDecimal = BigDecimal.valueOf(intergalacticUnitToInt(right, context)).multiply(metalSingleUnit);
            return left.interpret(context) + SPACE + String.format("%.0f", romanToDecimal) + SPACE + CREDITS;
        };
    }
}
