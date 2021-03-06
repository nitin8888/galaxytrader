package galaxy;

import galaxy.questions.HowManyCreditIs;
import galaxy.questions.HowMuchIs;
import galaxy.questions.Questions;
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
import static galaxy.util.ConversionUtil.intergalacticUnitToDecimal;
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
     * @param question the question asked
     * @return the response to be provided
     */
    String respond(String question) {
        try {
            List<String> response = getResponse(question);
            getMetalOptional(question).ifPresent(response::add);

            StringBuilder sb = new StringBuilder(String.join(SPACE, response));
            Optional<Expr> respStr = parse(question, response);

            respStr.ifPresent(r -> sb.append(SPACE).append(r.interpret(context)));
            return sb.toString();

        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return NO_IDEA_RESPONSE;
        }
    }

    private Optional<String> getMetalOptional(String question) {
        return Arrays.stream(question.split(SPACE)).filter(s -> metalSingleUnitMap.containsKey(s)).findAny();
    }

    private Optional<Expr> parse(String question, List<String> response) {
        List<Expr> respExpr = getRespExprList(response);

        return Arrays.stream(Questions.values()).filter(q -> question.startsWith(q.getDisplayValue())).findFirst()
                .map(q -> {
                    switch (q) {
                        case HOW_MUCH_IS:
                            return HowMuchIs.question(Expr.variable(q.getDisplayValue()), respExpr);
                        case HOW_MANY_CREDIT_IS:
                            String metal = getMetalOptional(question).orElseThrow(() -> new IllegalArgumentException("Metal not provided or invalid"));
                            return HowManyCreditIs.question(Expr.variable(q.getDisplayValue()), respExpr, getMetalSingleUnit(metal));
                    }
                    return null;
                });
    }

    private List<Expr> getRespExprList(List<String> response) {
        return response.stream().map(Expr::variable).collect(Collectors.toList());
    }

    private BigDecimal getMetalSingleUnit(String metal) {
        String interpret = Expr.variable(metal).interpret(metalSingleUnitMap);
        return NumberUtils.isCreatable(interpret) ? new BigDecimal(interpret) : BigDecimal.ONE;
    }

    private List<String> getResponse(String question) {
        List<String> response = Arrays.stream(question.split(SPACE)).filter(s -> context.containsKey(s)).collect(Collectors.toList());
        if (response.isEmpty()) {
            throw new IllegalArgumentException("Could not interpret question:" + question);
        }
        return response;
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
        int denominator = intergalacticUnitToDecimal(Arrays.asList(keySplit), context);
        return BigDecimal.valueOf(numerator).divide(BigDecimal.valueOf(denominator), 5, RoundingMode.HALF_UP).toPlainString();
    }
}
