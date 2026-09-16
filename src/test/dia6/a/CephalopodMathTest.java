package software.ulpgc.day06.a;

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

        assertThat(CephalopodMathCalculator.create().parse(input).compute()).isEqualTo(4277556L);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day06/a/math.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(CephalopodMathCalculator.create().parse(input).compute()).isEqualTo(6635273135233L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
