
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        s = s.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

        System.out.println(s);
    }
}