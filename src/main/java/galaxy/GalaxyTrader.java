package galaxy;

import galaxy.roman.RomanToNumberUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static galaxy.common.CommonText.*;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * The main class for Galaxy Trader to respond input and respond with appropriate output.
 */
class GalaxyTrader {

    private static final Logger LOG = Logger.getLogger(GalaxyTrader.class.getName());

    /**
     * The Interpreter design patter to store context used for interpreting
     */
    private Map<String, String> context;

    /**
     * The metal single unit credit.
     */
    private Map<String, String> metalSingleUnitMap;

    /**
     * Construct an instance of this class & initialize Context
     * @param input the input to initialize the Context
     */
    GalaxyTrader(List<String> input) {
        initContextMaps(input);
    }

    /**
     * Respond to a particular question.
     * @param questions the question asked
     * @return the response to be provided
     */
    String respond(String questions) {
        try {
            List<String> response = getResponse(questions);
            if (response.isEmpty()) {
                return NO_IDEA;
            }
            getMetalOptional(questions).ifPresent(response::add);

            StringBuilder sb = new StringBuilder(String.join(SPACE, response));
            Optional<Expr> respStr = parse(questions, response);

            respStr.ifPresent(r -> sb.append(SPACE).append(r.interpret(context)));
            return sb.toString();

        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return NO_IDEA;
        }
    }

    private Optional<String> getMetalOptional(String questions) {
        return Arrays.stream(questions.split(SPACE)).filter(s -> metalSingleUnitMap.containsKey(s)).findAny();
    }

    private Optional<Expr> parse(String questions, List<String> response) {
        List<Expr> respExpr = getRespExprList(response);

        return Arrays.stream(Questions.values()).filter(q -> questions.startsWith(q.getDisplayValue())).findFirst()
                .map(q -> {
                    switch (q) {
                        case HOW_MUCH_IS:
                            return Expr.howMuchIs(Expr.variable(q.getDisplayValue()), respExpr);
                        case HOW_MANY_CREDIT_IS:
                            String metal = getMetalOptional(questions).orElseThrow(() -> new IllegalArgumentException("Metal not provided or invalid"));
                            return Expr.howManyCreditIs(Expr.variable(q.getDisplayValue()), respExpr, getMetalSingleUnit(metal));
                    }
                    return null;
                });
    }

    private List<Expr> getRespExprList(List<String> response) {
        return response.stream().map(Expr::variable).collect(Collectors.toList());
    }

    private int intergalacticUnitToDecimal(List<String> words) {
        String roman = words.stream().map(s -> Expr.variable(s).interpret(context)).collect(Collectors.joining());
        return RomanToNumberUtil.convert(roman);
    }

    private BigDecimal getMetalSingleUnit(String metal) {
        String interpret = Expr.variable(metal).interpret(metalSingleUnitMap);
        return NumberUtils.isCreatable(interpret) ? new BigDecimal(interpret) : BigDecimal.ONE;
    }

    private List<String> getResponse(String questions) {
        return Arrays.stream(questions.split(SPACE)).filter(s -> context.containsKey(s)).collect(Collectors.toList());
    }

    private void initContextMaps(List<String> input) {
        context = input.stream().collect(Collectors.toMap(s -> s.split(IS_SPACE)[0], s -> s.split(IS_SPACE)[1]));
        metalSingleUnitMap = context.entrySet().stream().filter(me -> me.getKey().contains(SPACE)).collect(Collectors.toMap(s -> {
            String[] split = s.getKey().split(SPACE);
            return split[split.length - 1];
        }, this::convertToSingleUnit));
        Arrays.stream(Questions.values()).forEach(q -> context.put(q.getDisplayValue(), IS));
    }

    private String convertToSingleUnit(Map.Entry<String, String> entry) {
        String[] keySplit = entry.getKey().split(SPACE);
        String[] nosUnits = entry.getValue().split(SPACE);
        int numerator = Integer.parseInt(nosUnits[0]);
        int denominator = intergalacticUnitToDecimal(Arrays.asList(keySplit));
        return BigDecimal.valueOf(numerator).divide(BigDecimal.valueOf(denominator), 5, RoundingMode.HALF_UP).toPlainString();
    }
}
