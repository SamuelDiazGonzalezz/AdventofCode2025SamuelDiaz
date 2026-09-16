package software.ulpgc.day05.a;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryManagementTest {
    @Test
    public void test_example_aoc() {
        String aoc_example = """
                3-5
                10-14
                16-20
                12-18
                
                1
                5
                8
                11
                17
                32
                """;
        assertThat(InventoryManagement.create().parse(aoc_example).count()).isEqualTo(3);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day05/a/inventory.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(InventoryManagement.create().parse(input).count()).isEqualTo(888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
