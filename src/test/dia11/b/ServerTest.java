package dia11.b;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest {
    @Test
    public void aoc_example() {
        String input = """
                svr: aaa bbb
                aaa: fft
                fft: ccc
                bbb: tty
                tty: ccc
                ccc: ddd eee
                ddd: hub
                hub: fff
                eee: dac
                dac: fff
                fff: ggg hhh
                ggg: out
                hhh: out
                """;
        assertThat(ServerRack.create().parse(input).pathsTo("svr", "out")).isEqualTo(2);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day11/b/devices.txt")) {
            Assert.assertNotNull(is);
            String input = new String(is.readAllBytes());
            assertThat(ServerRack.create().parse(input).pathsTo("svr", "out")).isEqualTo(506264456238938L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
