package software.ulpgc.day02.a;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class IDTest {
    @Test
    public void basic_test_with_fluid_construction() {
        assertThat(Classifier.create().from(11).to(22).calculate().sum()).isEqualTo(33);
    }

    @Test
    public void example_test_with_fluid_construction() {
        String ranges = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";
        assertThat(ClassifierAdder.create().compile(ranges).sum()).isEqualTo(1227775554);
    }

    @Test
    public void file_test_with_fluid_construction() {
        try (InputStream is = getClass().getResourceAsStream("/day02/a/ranges.txt")) {
            Assert.assertNotNull(is);
            String ranges = new String(is.readAllBytes());
            assertThat(ClassifierAdder.create().compile(ranges).sum()).isEqualTo(18952700150L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
