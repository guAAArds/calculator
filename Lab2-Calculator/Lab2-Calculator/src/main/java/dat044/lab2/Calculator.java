package dat044.lab2;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in it's
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
public class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.isEmpty()) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    // ------  Evaluate RPN expression -------------------

    double evalPostfix(List<String> postfix) {
        // TODO
        List<String> stack = new ArrayList<>();
        for(int i = 0; i < postfix.size(); i++){
            String chString = postfix.get(i);
            if("+-/*^".contains(chString)){
                if(stack.size() >= 2){
                    double result = applyOperator(chString, Double.parseDouble(stack.get(stack.size()-1)), Double.parseDouble(stack.get(stack.size()-2)));
                    stack.remove(stack.size()-1);
                    stack.remove(stack.size()-1);
                    String add = result + "";
                    stack.add(add);
                }
                else {
                    throw new IllegalArgumentException(MISSING_OPERAND);
                }

            }
            else{
                stack.add(chString);
            }
        }


        if(stack.size() > 1){
            throw new IllegalArgumentException(MISSING_OPERATOR);
        }
        return Double.parseDouble(stack.get(0));
    }

    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    List<String> infix2Postfix(List<String> infix){
        List<String> stack = new ArrayList<>();
        List<String> postfix = new ArrayList<>();

        for(int i = 0; i < infix.size(); i++){
            String chString = infix.get(i);
            char ch = chString.charAt(0);

            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')){
                postfix.add(chString);
            }
            else if (ch == '('){
                stack.add(chString);
            }
            else if (ch == ')') {
                while(!stack.get(stack.size()-1).equals("(")){
                    postfix.add(stack.get((stack.size()-1)));
                    stack.remove(stack.size()-1);
                    if(stack.isEmpty()){
                        throw new IllegalArgumentException(MISSING_OPERATOR);
                    }
                }
                stack.remove(stack.size()-1);
            }
            else {
                while (!stack.isEmpty() && getPrecedence(chString) <= getPrecedence(stack.get(stack.size()-1)) && getPrecedence(chString) != 4){
                    postfix.add(stack.get(stack.size()-1));
                    stack.remove((stack.size()-1));
                }
                stack.add(chString);
            }
        }
        while (!stack.isEmpty()){
            postfix.add(stack.get(stack.size()-1));
            stack.remove(stack.size()-1);
        }

        return postfix;
    }

    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        }else if("(".contains(op)){
            return  1;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    // ---------- Tokenize -----------------------

    // List String (not char) because numbers (with many chars)
    List<String> tokenize(String expr) {
        // TODO
        List<String> returning = new ArrayList<>();
        String adding = "";

        for(int i = 0; i != expr.length(); i++){
            char ch = expr.charAt(i);
            if("+-*/()^".contains(String.valueOf(ch))){
                if(!adding.isEmpty()){
                    returning.add(adding);
                    adding = "";
                }
                returning.add(String.valueOf(ch));
            }
            else if("0123456789".contains(String.valueOf(ch))){
                adding += ch;
            }
            else{
                if(!adding.isEmpty()){
                    returning.add(adding);
                    adding = "";
                }
            }
        }
        if(!adding.isEmpty()){
            returning.add(adding);
        }
        return returning;
    }
}