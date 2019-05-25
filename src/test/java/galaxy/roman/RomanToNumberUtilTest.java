package galaxy.roman;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static galaxy.roman.RomanToNumberUtil.convert;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RomanToNumberUtilTest {

    @Test
    void romanToIntSingleTest() {
        assertThat(convert(Roman.I.name()), is(1));
        assertThat(convert(Roman.V.name()), is(5));
        assertThat(convert(Roman.X.name()), is(10));
        assertThat(convert(Roman.L.name()), is(50));
        assertThat(convert(Roman.C.name()), is(100));
        assertThat(convert(Roman.D.name()), is(500));
        assertThat(convert(Roman.M.name()), is(1000));
    }

    @Test
    void romanToIntCombineTest() {
        assertThat(convert("MMVI"), is(2006));
    }

    @Test
    void romanToIntSmallerValueTest() {
        assertThat(convert("VI"), is(6));
        assertThat(convert("MC"), is(1100));
        assertThat(convert("MCM"), is(1900));
        assertThat(convert("MCMIII"), is(1903));
        assertThat(convert("MCMXLIV"), is(1944));
    }

    @Test
    void romanToIntInvalidValueTest() {
        List<String> invalidValues = Arrays.asList(
                "IIII", "XXXX", "CCCC", "MMMM"                              // "I", "X", "C", and "M" cannot be repeated more than 3 times
                , "DD", "LL", "VV"                                          // "D", "L", and "V" can never be repeated
                , "IL", "IC", "ID", "IM"                                    // "I" can be subtracted from "V" and "X" only.
                , "XD", "XM"                                                // "X" can be subtracted from "L" and "C" only
                , "VX", "VL", "VC", "VD", "VM", "LC", "LD", "LM", "DM");    // "V", "L", and "D" can never be subtracted

        invalidValues.forEach(v -> assertThrows(IllegalArgumentException.class, () -> convert(v)));
    }
}
