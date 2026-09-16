package software.ulpgc.day06.b;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CephalopodMathTest {
    @Test
    public void example_aoc() {
        String input = """
                123 328  51 64
                 45 64  387 23
                  6 98  215 314
                *   +   *   + 
                """;

        assertThat(CephalopodMathCalculator.create().parse(input).compute()).isEqualTo(3263827L);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day06/b/math.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(CephalopodMathCalculator.create().parse(input).compute()).isEqualTo(12542543681221L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
