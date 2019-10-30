package core.common;


import core.symbols.Literal;

import java.util.Map;

public class Utilities {

    public static String stripAllSpaces(String str) {
        return str.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint,
                StringBuilder::append).toString();
    }

    public static void printMap(Map<Literal, Boolean> map) {
        System.out.println("== Interpretation ==");
        for (Map.Entry e : map.entrySet()) {
            System.out.println("|- " + ((Literal) e.getKey()).getRaw() + " -> " + e.getValue());
        }
        System.out.println("== == == == == == ==");
    }
}
