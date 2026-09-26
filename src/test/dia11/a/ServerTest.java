package dia11.a;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest {
    @Test
    public void aoc_example() {
        String input = """
                aaa: you hhh
                you: bbb ccc
                bbb: ddd eee
                ccc: ddd eee fff
                ddd: ggg
                eee: out
                fff: out
                ggg: out
                hhh: ccc fff iii
                iii: out
                """;
        assertThat(ServerRack.create().parse(input).pathsTo("you", "out")).isEqualTo(5);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day11/a/devices.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(ServerRack.create().parse(input).pathsTo("you", "out")).isEqualTo(607);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
