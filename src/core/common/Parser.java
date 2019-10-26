package core.common;

import core.exceptions.InvalidFormulaException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    public static void main(String[] args) {
        Parser p = new Parser();
        p.evaluate("(Chicken /\\ (Tiger -> cat)) <-> Snake \\/ Dog ->)");
        System.out.println(p);
    }

    private int i;
    private List<String> parsed_tokens;
    private StringBuilder curr_token;
    private String curr_input_str;
    private Stack<Character> brackets_stack;

    public Parser() {
        reset(null);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : parsed_tokens) {
            sb.append(s).append('\n');
        }
        return sb.toString();
    }

    @Contract(" _ -> fail")
    private void handle_error(String msg) {
        System.err.println(msg);
        System.err.println(curr_input_str);
        while (--i > 0) System.err.print(' ');
        System.err.println("^^");
        System.exit(-10);
    }

    private void checkLiteral(@NotNull Symbol prev) {
        if (prev == Symbol.NULL || prev == Symbol.RBRACKET || prev == Symbol.LITERAL) {
            handle_error("Literal not allowed here");
        }
    }

    private void checkLeftBracket(Symbol prev) {
        checkNegation(prev);  // same
    }

    private void checkRightBracket(Symbol prev) {
        if (prev != Symbol.LITERAL && prev != Symbol.RBRACKET) {
            handle_error("Right bracket not allowed here");
        }
    }

    private void checkConnective(Symbol prev) {
        if (prev != Symbol.LITERAL && prev != Symbol.RBRACKET) {
            handle_error("Connective not allowed here");
        }
    }

    private void checkNegation(@NotNull Symbol prev) {
        if (prev == Symbol.NULL || prev == Symbol.LITERAL || prev == Symbol.RBRACKET) {
            handle_error("Negation not allowed here");
        }
    }

    private void endToken() {
        parsed_tokens.add(curr_token.toString());
        curr_token = new StringBuilder();
    }

    private void reset(String newStr) {
        i = 0;
        parsed_tokens = new ArrayList<>();
        curr_token = new StringBuilder();
        curr_input_str = newStr;
        brackets_stack = new Stack<>();
    }

    public void evaluate(String s) throws InvalidFormulaException {
        if (s == null) {
            throw new InvalidFormulaException("Propositional Logic formula can't be null");
        }
        reset(s);
        char[] chars = curr_input_str.toCharArray();
        Symbol last_char = Symbol.START;
        while (i < chars.length) {
            char c = chars[i];
            if (c == ' ') {
                i++;
                continue;
            }
            switch (c) {
                case '~':
                    checkNegation(last_char);
                    curr_token.append(c);
                    last_char = Symbol.NEG;
                    i++;
                    break;
                case '(':
                    checkLeftBracket(last_char);
                    curr_token.append('(');
                    last_char = Symbol.LBRACKET;
                    i++;
                    brackets_stack.push('(');
                    break;
                case ')':
                    checkRightBracket(last_char);
                    curr_token.append(')');
                    endToken();
                    last_char = Symbol.RBRACKET;
                    i++;
                    endToken();
                    if (brackets_stack.empty() || brackets_stack.pop() != '(') {
                        handle_error("Unopened closing bracket");
                    }
                    break;
                case '/':
                    checkConnective(last_char);
                    if (i + 1 >= chars.length || chars[i+1] != '\\') {  // must be AND /\
                        handle_error("Invalid character: \"/\", do you mean /\\?");
                    } else {
                        curr_token.append("/\\");
                        i += 2;
                    }
                    last_char = Symbol.CONNECTIVE;
                    break;
                case '\\':
                    checkConnective(last_char);
                    if (i + 1 >= chars.length || chars[i+1] != '/') {  // must be OR \/
                        handle_error("Invalid character: \"\\\", do you mean \\/?");
                    } else {
                        curr_token.append("\\/");
                        i += 2;
                    }
                    last_char = Symbol.CONNECTIVE;
                    break;
                case '-':
                    if (i - 1 >= 0 && chars[i- 1] != '<') {
                        checkConnective(last_char);
                    }
                    if (i + 1 >= chars.length || chars[i+1] != '>') {  // must be ->
                        handle_error("Invalid character: \"-\", do you mean ->?");
                    } else {
                        curr_token.append("->");
                        i += 2;
                    }
                    last_char = Symbol.CONNECTIVE;
                    break;
                case '<':
                    checkConnective(last_char);
                    if (i + 1 >= chars.length || chars[i+1] != '-') {  // must be <-
                        handle_error("Invalid character: \"<\", do you mean <->?");
                    } else {
                        curr_token.append("<");  // will append -> in the next loop
                        i++;
                    }
                    last_char = Symbol.NULL;
                    break;
                default:
                    if (Character.isLetter(c)) {
                        checkLiteral(last_char);
                        while (i < chars.length && Character.isLetter(chars[i])) {
                            curr_token.append(chars[i++]);
                        }
                        endToken();
                        last_char = Symbol.LITERAL;
                    } else {
                        handle_error(String.format("Invalid character: \"%s\"", c));
                    }
                    break;
            }  // switch
        } // while
        if (curr_token.length() != 0) {
            handle_error("Incomplete clause");
        }
        if (!brackets_stack.empty()) {
            handle_error("Unclosed bracket");
        }

    }

    private enum Symbol {
        NEG, RBRACKET, LBRACKET, LITERAL, CONNECTIVE, START, NULL  // null does not match anything
    }

}
