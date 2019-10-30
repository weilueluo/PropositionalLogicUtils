package core.common;


public class Utilities {

    public static String stripAllSpaces(String str) {
        return str.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint,
                StringBuilder::append).toString();
    }
}
