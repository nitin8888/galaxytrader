package galaxy.questions;

import galaxy.Expr;

import java.util.List;

import static galaxy.util.ConversionUtil.intergalacticUnitToInt;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * How much is question
 */
public class HowMuchIs {

    /**
     * How much is question rule
     */
    public static Expr question(Expr left, List<Expr> right) {
        return context -> left.interpret(context) + SPACE + intergalacticUnitToInt(right, context);
    }
}
