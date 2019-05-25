package galaxy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class GalaxyTraderTest {

    @Test
    void galaxyTraderTest() {
        GalaxyTrader galaxyTrader = getGalaxyTrader();
        assertThat(galaxyTrader.respond("how much is pish tegj glob glob ?"), is("pish tegj glob glob is 42"));
        assertThat(galaxyTrader.respond("how many Credits is glob prok Silver ?"), is("glob prok Silver is 68 Credits"));
        assertThat(galaxyTrader.respond("how many Credits is glob prok Gold ?"), is("glob prok Gold is 57800 Credits"));
        assertThat(galaxyTrader.respond("how many Credits is glob prok Iron ?"), is("glob prok Iron is 782 Credits"));
        assertThat(galaxyTrader.respond("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?"),
                is("I have no idea what you are talking about"));
    }

    @Test
    void galaxyTraderInvalidAmountTest() {
        GalaxyTrader galaxyTrader = getGalaxyTrader();
        assertThat(galaxyTrader.respond("how much is pish pish pish pish glob glob ?"),
                is("I have no idea what you are talking about"));
        assertThat(galaxyTrader.respond("how many Credits is tegj tegj Gold ?"),
                is("I have no idea what you are talking about"));
    }

    @Test
    void galaxyTraderNoAmountTest() {
        GalaxyTrader galaxyTrader = getGalaxyTrader();
        assertThat(galaxyTrader.respond("how much is ?"), is("I have no idea what you are talking about"));
        assertThat(galaxyTrader.respond("how many Credits is Gold ?"), is("I have no idea what you are talking about"));
    }

    @Test
    void galaxyTraderNoMetalTest() {
        GalaxyTrader galaxyTrader = getGalaxyTrader();
        assertThat(galaxyTrader.respond("how many Credits is glob prok ?"), is("I have no idea what you are talking about"));
    }

    @Test
    void galaxyTraderInvalidMetalTest() {
        GalaxyTrader galaxyTrader = getGalaxyTrader();
        assertThat(galaxyTrader.respond("how many Credits is glob prok alluminium ?"), is("I have no idea what you are talking about"));
    }

    private GalaxyTrader getGalaxyTrader() {
        List<String> input = Arrays.asList("glob is I"
                , "prok is V"
                , "pish is X"
                , "tegj is L"
                , "glob glob Silver is 34 Credits"
                , "glob prok Gold is 57800 Credits"
                , "pish pish Iron is 3910 Credits"
        );
        return new GalaxyTrader(input);
    }
}
