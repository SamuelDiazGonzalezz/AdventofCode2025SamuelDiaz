package dia8.a;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ChristmasDecorationTest {
    @Test
    public void aco_example() {
        String input = """
                162,817,812
                57,618,57
                906,360,560
                592,479,940
                352,342,300
                466,668,158
                542,29,236
                431,825,988
                739,650,466
                52,470,668
                216,146,977
                819,987,18
                117,168,530
                805,96,715
                346,949,466
                970,615,88
                941,993,340
                862,61,35
                984,92,344
                425,690,689
                """;
        assertThat(ChristmasDecorationManager.create(10).parse(input).startConnections().calculate()).isEqualTo(40);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day08/a/positions.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(ChristmasDecorationManager.create(1000).parse(input).startConnections().calculate()).isEqualTo(140008);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
