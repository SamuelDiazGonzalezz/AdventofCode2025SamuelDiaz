package software.ulpgc.day03.a;

import org.junit.Assert;
import org.junit.Test;


import java.io.*;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BatteryBankTest {
    @Test
    public void basic_test_example() {
        String input = """
                            987654321111111
                            811111111111119
                            234234234234278
                            818181911112111
                        """;
        assertThat(BatteryMaximizer.create().fromString(Arrays.asList(input.split("\n"))).sum()).isEqualTo(357);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day03/a/batterybanks.txt")) {
            Assert.assertNotNull(is);
            String batteryBanksString = new String(is.readAllBytes());
            assertThat(BatteryMaximizer.create().fromString(Arrays.asList(batteryBanksString.split("\n"))).sum()).isEqualTo(16812);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
