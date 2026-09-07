package software.ulpgc.day02.a;

public record ID(String number) {

    public boolean isValid () {
        return !number.startsWith("0") &&
                (number.length() % 2 == 1 ||
                        !number.substring(0, number.length()/2)
                                .equals(number.substring(number.length()/2)));
    }

    public long longNumber () {
        return Long.parseLong(number());
    }
}
