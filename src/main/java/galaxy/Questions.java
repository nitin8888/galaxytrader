package galaxy;

/**
 * The questions asked by aliens
 */
public enum Questions {
    HOW_MUCH_IS("how much is "),
    HOW_MANY_CREDIT_IS("how many Credits is ");

    /**
     * The display value
     */
    private final String displayValue;

    Questions(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
