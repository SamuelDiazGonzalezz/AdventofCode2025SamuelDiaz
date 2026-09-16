package software.ulpgc.day05.b;

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
                """;
        assertThat(InventoryManagement.create().parse(aoc_example).count()).isEqualTo(14);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day05/b/inventory.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(InventoryManagement.create().parse(input).count()).isEqualTo(344378119285354L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
