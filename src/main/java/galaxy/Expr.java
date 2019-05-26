package galaxy;

import java.util.Map;

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
     * Variable rule
     */
    static Expr variable(String name) {
        return context -> context.getOrDefault(name, "");
    }
}
