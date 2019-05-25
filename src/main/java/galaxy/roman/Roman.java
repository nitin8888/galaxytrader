package galaxy.roman;

/**
 * Enum for Roman characters and values
 */
public enum Roman {
    I(1),
    V(5),
    X(10),
    L(50),
    C(100),
    D(500),
    M(1000);

    /**
     * Roman character number value
     */
    private final int value;

    Roman(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
