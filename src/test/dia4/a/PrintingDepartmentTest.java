package software.ulpgc.day04.a;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PrintingDepartmentTest {
    @Test
    public void exaple_test() {
        String testContent = """
                ..@@.@@@@.
                @@@.@.@.@@
                @@@@@.@.@@
                @.@@@@..@.
                @@.@@@@.@@
                .@@@@@@@.@
                .@.@.@.@@@
                @.@@@.@@@@
                .@@@@@@@@.
                @.@.@@@.@.
                """;
        String[] rows = testContent.trim().split("\n");
        assertThat(PrintingDepartment
                .create(new Position(rows[0].length(), rows.length))
                .addRows(rows)
                .process()
                .accesibleRollsCount()).isEqualTo(13);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day04/a/paperrolls.txt")) {
            Assert.assertNotNull(is);
            String[] rows = (new String(is.readAllBytes()).trim()).split("\n");
            assertThat(PrintingDepartment
                    .create(new Position(rows[0].length(), rows.length))
                    .addRows(rows)
                    .process()
                    .accesibleRollsCount()).isEqualTo(1395);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
