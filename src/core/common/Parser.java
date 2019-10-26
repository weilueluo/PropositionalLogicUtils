package core.common;

import core.exceptions.InvalidFormulaException;
import org.jetbrains.annotations.Contract;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    public static void main(String[] args) {
        Parser p = new Parser();
        Instant start = Instant.now();
        p.evaluate("(Chicken /\\ (Tiger -> cat) <-> Snake \\/ Dog -> Cat /\\ Snake <-> Duck) \\/ Monkey /\\ Fish");
        Instant end = Instant.now();
        System.out.println((String.format("Runtime: %sms", Duration.between(start, end).toMillis())));
        System.out.println(p);
        for(String s : p.getTokens()) {
            System.out.println(String.format("Token: \"%s\"", s));
        }
    }

    private final char NEG = '~';
    private final char DASH = '-';
    private final char LESS_THAN = '<';
    private final char GREATER_THAN = '>';
    private final char LEFT_BRACKET = '(';
    private final char RIGHT_BRACKET = ')';
    private final char BACKWARD_SLASH = '\\';
    private final char FORWARD_SLASH = '/';
    private final String AND = "/\\";
    private final String OR = "\\/";
    private final String IMPLIES = "->";
    private final String IFF = "<->";

    private int i;
    private List<String> parsed_tokens;
    private StringBuilder curr_token_builder;
    private String curr_input_str;
    private char[] chars;  // char array form of current input string
    private Stack<Character> brackets_stack;
    private Symbol prev_char;

    public Parser() {
        reset(null);
    }

    private void reset(String newStr) {
        i = 0;
        parsed_tokens = new ArrayList<>();
        curr_token_builder = new StringBuilder();
        curr_input_str = newStr;
        brackets_stack = new Stack<>();
        prev_char = Symbol.START;
        chars = curr_input_str == null ? null : curr_input_str.toCharArray();
    }

    public void evaluate(String s) throws InvalidFormulaException {
        if (s == null) {
            throw new InvalidFormulaException("Propositional Logic formula can't be null");
        }
        reset(s);
        while (i < chars.length) {
            char c = chars[i];
            if (c == ' ') {
                i++;
                continue;
            }
            switch (c) {
                case '~': handleNegation(); break;
                case '(': handleLeftBracket(); break;
                case ')': handleRightBracket(); break;
                case '/': handleForwardSlash(); break;
                case '\\': handleBackwardSlash(); break;
                case '-': handleDash(); break;
                case '<': handleLessThan(); break;
                default:
                    if (Character.isLetter(c)) {
                        handleCharacter();
                    } else {
                        handle_error(String.format("Invalid character: \"%s\"", c));
                    }
                    break;
            }  // switch
        } // while

        if (prev_char == Symbol.START) {
            handle_error("Propositional logic formula can't be empty");
        }
        if (curr_token_builder.length() != 0) {
            handle_error("Incomplete clause");
        }
        if (!brackets_stack.empty()) {
            handle_error("Unclosed bracket");
        }

    }

    public List<String> getTokens() {
        return parsed_tokens;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : parsed_tokens) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Contract(" _ -> fail")
    private void handle_error(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg).append(System.lineSeparator()).append(curr_input_str).append(System.lineSeparator());
        while (--i > 0) sb.append(' ');
        sb.append(String.format("^^%n"));
        throw new InvalidFormulaException(sb.toString());
    }

    private void checkConnective(Symbol prev) {
        if (prev != Symbol.LITERAL && prev != Symbol.RBRACKET) {
            handle_error("Connective not allowed here");
        }
    }

    private void endToken() {
        parsed_tokens.add(curr_token_builder.toString());
        curr_token_builder = new StringBuilder();
    }

    private void handleNegation() {
        if (prev_char == Symbol.NULL || prev_char == Symbol.LITERAL || prev_char == Symbol.RBRACKET) {
            handle_error("Negation not allowed here");
        }
        curr_token_builder.append(NEG);
        prev_char = Symbol.NEG;
        i++;
    }

    private void handleLeftBracket() {
        if (prev_char == Symbol.NULL || prev_char == Symbol.LITERAL || prev_char == Symbol.RBRACKET) {
            handle_error("Left bracket not allowed here");
        }
        curr_token_builder.append(LEFT_BRACKET);
        prev_char = Symbol.LBRACKET;
        brackets_stack.push(LEFT_BRACKET);
        endToken();
        i++;
    }

    private void handleRightBracket() {
        if (prev_char != Symbol.LITERAL && prev_char != Symbol.RBRACKET) {
            handle_error("Right bracket not allowed here");
        }
        if (brackets_stack.empty() || brackets_stack.pop() != LEFT_BRACKET) {
            handle_error("Unopened closing bracket");
        }
        curr_token_builder.append(RIGHT_BRACKET);
        endToken();
        prev_char = Symbol.RBRACKET;
        i++;
    }

    private void handleForwardSlash() {
        checkConnective(prev_char);
        if (i + 1 >= chars.length || chars[i+1] != BACKWARD_SLASH) {  // must be AND /\
            handle_error(String.format("Invalid character: \"%s\", do you mean %s?", FORWARD_SLASH, AND));
        } else {
            curr_token_builder.append(AND);
            endToken();
            prev_char = Symbol.CONNECTIVE;
            i += 2;
        }
    }

    private void handleBackwardSlash() {
        checkConnective(prev_char);
        if (i + 1 >= chars.length || chars[i+1] != '/') {  // must be OR \/
            handle_error(String.format("Invalid character: \"%s\", do you mean %s?", BACKWARD_SLASH, OR));
        } else {
            curr_token_builder.append(OR);
            endToken();
            prev_char = Symbol.CONNECTIVE;
            i += 2;
        }
    }

    private void handleDash() {
        // if followed by <, then it is already checked that we can have connective
        if (i - 1 >= 0 && chars[i- 1] != LESS_THAN) {
            checkConnective(prev_char);
        }
        if (i + 1 >= chars.length || chars[i+1] != GREATER_THAN) {  // must be ->
            handle_error(String.format("Invalid character: \"%s\", do you mean %s?", DASH, IMPLIES));
        }
        curr_token_builder.append(IMPLIES);
        endToken();
        prev_char = Symbol.CONNECTIVE;
        i += 2;
    }

    private void handleLessThan() {
        checkConnective(prev_char);
        if (i + 1 >= chars.length || chars[i+1] != DASH) {  // must be <-
            handle_error(String.format("Invalid character: \"%s\", do you mean %s?", LESS_THAN, IFF));
        } else {
            curr_token_builder.append(LESS_THAN);  // will append -> in the next loop
            prev_char = Symbol.NULL;
            endToken();
            i++;
        }
    }

    private void handleCharacter() {
        if (prev_char == Symbol.NULL || prev_char == Symbol.RBRACKET || prev_char == Symbol.LITERAL) {
            handle_error("Literal not allowed here");
        }
        while (i < chars.length && Character.isLetter(chars[i])) {
            curr_token_builder.append(chars[i++]);
        }
        endToken();
        prev_char = Symbol.LITERAL;
    }

    private enum Symbol {
        NEG, RBRACKET, LBRACKET, LITERAL, CONNECTIVE, START, NULL  // null does not match anything
    }

}
