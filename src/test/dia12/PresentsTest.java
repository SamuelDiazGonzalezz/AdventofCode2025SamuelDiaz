package dia12;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PresentsTest {

    @Test
    public void aoc_example() {
        String input = """
                0:
                ###
                ##.
                ##.
                
                1:
                ###
                ##.
                .##
                
                2:
                .##
                ###
                ##.
                
                3:
                ##.
                ###
                ##.
                
                4:
                ###
                #..
                ###
                
                5:
                ###
                .#.
                ###
                
                4x4: 0 0 0 0 2 0
                12x5: 1 0 1 0 2 2
                12x5: 1 0 1 0 3 2
                """;
        assertThat(PresentFitter.create().parse(input).fittableRegions()).isEqualTo(2);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day12/worksheet.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(PresentFitter.create().parse(input).fittableRegions()).isEqualTo(519);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
