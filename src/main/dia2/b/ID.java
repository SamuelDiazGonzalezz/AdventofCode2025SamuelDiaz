package software.ulpgc.day02.b;

import java.util.stream.IntStream;

public record ID(String number) {

    public boolean isValid () {
        return number.length() <= 1 ||
                IntStream.range(1, number.length() / 2 + 1)
                        .filter(len -> number.length() % len == 0)
                        .noneMatch(len -> number.equals(number.substring(0, len).repeat(number.length() / len)));
    }

    public long longNumber () {
        return Long.parseLong(number());
    }
}
