package core.common;

import org.jetbrains.annotations.NotNull;

public class Utilities {

    @NotNull
    public static String stripAllSpaces(@NotNull String str) {
        return str.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint,
                StringBuilder::append).toString();
    }
}
