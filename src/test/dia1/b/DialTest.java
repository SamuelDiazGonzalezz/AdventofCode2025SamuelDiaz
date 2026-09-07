package software.ulpgc.day01.b;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class DialTest {
    private static final String orders = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
            """;

    @Test
    public void given_orders_should_account_the_final_position() {
        assertThat(Dial.create().add("L1").position()).isEqualTo(49);
        assertThat(Dial.create().add("L1", "R1", "R50").position()).isEqualTo(0);
        assertThat(Dial.create().add("L51", "L500").position()).isEqualTo(99);
        assertThat(Dial.create().execute(orders).position()).isEqualTo(32);
    }

    @Test
    public void given_orders_should_account_the_times_that_position_is_zero() {
        assertThat(Dial.create().execute(orders).count()).isEqualTo(3);
        assertThat(Dial.create().add("L1").count()).isEqualTo(0);
        assertThat(Dial.create().add("L1", "R1", "R50").count()).isEqualTo(1);
        assertThat(Dial.create().add("L51", "L500").count()).isEqualTo(0);
    }

    @Test
    public void single_rotations_and_wraparound() {
        assertThat(Dial.create().add("L50").position()).isEqualTo(0);
        assertThat(Dial.create().add("R50").position()).isEqualTo(0);
        assertThat(Dial.create().add("L51").position()).isEqualTo(99);
        assertThat(Dial.create().add("R51").position()).isEqualTo(1);
    }

    @Test
    public void large_distances_reduce_mod_100() {
        assertThat(Dial.create().add("L150").position()).isEqualTo(0); // 50 - 150 = -100 -> 0
        assertThat(Dial.create().add("R250").position()).isEqualTo(0); // 50 + 250 = 300 -> 0
        assertThat(Dial.create().add("L12345").position()).isEqualTo((50 - 12345 % 100 + 100) % 100);
        assertThat(Dial.create().add("R12345").position()).isEqualTo((50 + 12345 % 100) % 100);
    }

    @Test
    public void sequence_with_multiple_zero_hits_and_counts() {
        String seq = """
                R50
                L100
                R200
                L150
                R50
                """;
        assertThat(Dial.create().execute(seq).position()).isEqualTo(0);
        assertThat(Dial.create().execute(seq).count()).isEqualTo(4);
    }

    @Test
    public void empty_and_single_command_counts() {
        assertThat(Dial.create().execute("").position()).isEqualTo(50);
        assertThat(Dial.create().execute("").count()).isEqualTo(0);
        assertThat(Dial.create().add("R50").count()).isEqualTo(1);
        assertThat(Dial.create().add("L50").count()).isEqualTo(1);
    }

    @Test
    public void alternating_small_moves() {
        String seq = """
                L1
                R1
                L1
                R1
                L1
                R1
                """;
        assertThat(Dial.create().execute(seq).position()).isEqualTo(50);
        assertThat(Dial.create().execute(seq).count()).isEqualTo(0);
    }

    @Test
    public void cumulative_sequence_matches_step_by_step() {
        Dial d = Dial.create();
        d.add("R10");
        d.add("L20");
        d.add("R30");
        int stepPosition = d.position();

        Dial d2 = Dial.create();
        d2.execute("""
                R10
                L20
                R30
                """);
        assertThat(d2.position()).isEqualTo(stepPosition);
        assertThat(d2.count()).isEqualTo(d.count());
    }

    @Test
    public void multiple_consecutive_zero_hits() {
        String seq = """
                R50
                R100
                L50
                L100
                """;
        // R50 -> 0 (1)
        // R100 -> 0 (2)
        // L50 -> 50
        // L100 -> 50 ->  (no new zero)
        assertThat(Dial.create().execute(seq).count()).isEqualTo(2);
    }

    @Test
    public void zeros_example() {
        String seq = """
                L68
                L30
                R48
                L5
                R60
                L55
                L1
                L99
                R14
                L82
                """;
        assertThat(Dial.create().execute(seq).zeros()).isEqualTo(6);
    }

    @Test
    public void aoc_file() {
        try (InputStream is = getClass().getResourceAsStream("/day01/b/orders.txt")) {
            Assert.assertNotNull(is);
            String seq = new String(is.readAllBytes());
            assertThat(Dial.create().execute(seq).position()).isEqualTo(92);
            assertThat(Dial.create().execute(seq).count()).isEqualTo(1150);
            assertThat(Dial.create().execute(seq).zeros()).isEqualTo(6738);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
