package dia7.b;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class LaboratoriesTest {

    @Before
    public void setUp() throws IOException {
        BeamManager.resetInstance();
    }

    @Test
    public void aoc_example() {
       String input = """
               .......S.......
               ...............
               .......^.......
               ...............
               ......^.^......
               ...............
               .....^.^.^.....
               ...............
               ....^.^...^....
               ...............
               ...^.^...^.^...
               ...............
               ..^...^.....^..
               ...............
               .^.^.^.^.^...^.
               ...............
               """;
       assertThat(BeamManager.getInstance().parse(input).count()).isEqualTo(9);
       assertThat(BeamManager.getInstance().parse(input).splits()).isEqualTo(21);
       assertThat(BeamManager.getInstance().parse(input).timelines()).isEqualTo(40);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day07/b/puzzle.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(BeamManager.getInstance().parse(input).count()).isEqualTo(85);
            assertThat(BeamManager.getInstance().parse(input).splits()).isEqualTo(1687);
            assertThat(BeamManager.getInstance().parse(input).timelines()).isEqualTo(390684413472684L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
