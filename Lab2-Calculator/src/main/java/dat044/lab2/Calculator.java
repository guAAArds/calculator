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
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    // ------  Evaluate RPN expression -------------------

    double evalPostfix(List<String> postfix) {
        // TODO
        return 0;
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

    List<String> infix2Postfix(List<String> infix) {
        // TODO
        List<String> stack = new ArrayList<>();
        List<String> postfix = new ArrayList<>();
        for(String s : infix){
            if("+-*/()^".contains(s)){
                fixStack(stack, postfix, s);
            }
            else{
                postfix.add(s);
            }
        }
        while(stack.size() != 0){
            postfix.add(stack.get(stack.size()-1));
            stack.remove(stack.size()-1);
        }
        return postfix;
    }
    void fixStack(List<String> stack, List<String> postfix, String addStack){
        if(addStack.equals(")")){
            for(int i = stack.size()-1; i >= 0; i--){
                if(stack.get(i).equals("(")){
                    stack.remove(i);
                    for(int v = i; v <= stack.size()-1; v++){
                        postfix.add(stack.get(v));
                        stack.remove(v);
                    }
                    break;
                }
//                else{
//                    postfix.add(stack.get(i));
//                    stack.remove(i);
//                }
            }
        }
        else{
            int index = stack.size() - 1;
            boolean cont = true;
            while (cont){
                if(stack.size() == 0){
                    stack.add(addStack);
                    cont = false;
                }
                else if (getPrecedence(stack.get(index)) >= getPrecedence(addStack) && !addStack.equals("^")){
                    postfix.add((stack.get(index)));
                    stack.remove(stack.get(index));
                    if(index-- >= 0){
                        index--;
                    }
                    else{
                        cont = false;
                    }
                }
                else {
                    stack.add(addStack);
                    cont = false;
                }
            }
        }
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
        List<String> r = new ArrayList<>();
        String adding = "";

        for(int i = 0; i != expr.length(); i++){
            char ch = expr.charAt(i);
            if("+-*/()^".contains(String.valueOf(ch))){
                if(adding != ""){
                    r.add(adding);
                    adding = "";
                }
                r.add(String.valueOf(ch));
            }
            else if("0123456789".contains(String.valueOf(ch))){
                adding += ch;
            }
            else{
                if(adding != ""){
                    r.add(adding);
                    adding = "";
                }
            }
        }
        if(adding != ""){
            r.add(adding);
            adding = "";
        }
        return r;
    }

}
