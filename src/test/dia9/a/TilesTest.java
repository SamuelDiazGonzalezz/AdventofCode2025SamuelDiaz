package dia9.a;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class TilesTest {
    @Test
    public void example_test() {
        String input = """
                7,1
                11,1
                11,7
                9,7
                9,5
                2,5
                2,3
                7,3
                """;
        assertThat(TilesManager.create().parse(input).rectangles().largestArea()).isEqualTo(50);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day09/a/tiles.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(TilesManager.create().parse(input).rectangles().largestArea()).isEqualTo(4715966250L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
