package ru.dyusov;

import java.util.HashMap;
import java.util.Stack;

public class Parser {
    protected HashMap<String, String> first = new HashMap<>() {{
        put("expression", "ic(");
        put("simple_expression", "+-ic(");
        put("simple_expression_stroke", "+-0Re");
        put("simple_expression_brackets", "(");
        put("term", "ic(");
        put("factor", "ic(");
        put("identifier", "i");
        put("constant", "c");
        put("term_stroke", "*/DIVMODANDe");
        put("op_relationship", "<=>");
        put("op_sum", "+-0R");
        put("op_mult", "*/DIVMODAND");
        put("sign", "+-");
        put("not_factor", "NOT");
    }};

    protected Stack<String> stack = new Stack<>();
    protected String postfix = "";
    protected String current = "";

    private int i = 0;
    private String s = null;

    public void run() throws Exception {
        expression();
    }

    public boolean eval(String str) {
        i = 0;
        s = str;

        boolean res = false;
        try {
            run();
            res = i == s.length();
            // push everything left in stack to postfix
            while (!stack.isEmpty()) {
                postfix = new StringBuffer(postfix).append(stack.pop()).toString();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return res;
    }

    protected boolean inFirst(String elem) {
        if (i >= s.length())
            return false;

        String curSymbol = Character.toString(s.charAt(i));

        return first.get(elem).contains(curSymbol);
    }

    protected void subStrStartWithTerm(String... items) throws Exception {
        for (String item : items)
            if (s.substring(i).startsWith(item)) {
                int old = i;
                i += item.length();
                current = s.substring(old, i);
                System.out.println("current: " + current);
                System.out.println("postfix: " + postfix);
                System.out.println("stack: " + stack);
                return;
            }
        throw new Exception(String.format("Unexpected symbol in input string, position=%d", i));
    }

    protected void expression() throws Exception {
        System.out.println("expression");
        if (inFirst("simple_expression")) {
            System.out.println("-> simpleExpression");
            simpleExpression();
            if (inFirst("op_relationship")) {
                System.out.println("-> op_relationship");
                opRelationship();
                System.out.println("-> simpleExpression");
                simpleExpression();
            }
        } else
            throw new Exception(String.format("Unexpected symbol in input string, position=%d", i));
    }

    private void simpleExpression() throws Exception {
        if (inFirst("sign")) {
            System.out.println("    -> sign");
            sign();
        }
        System.out.println("    -> term");
        term();
        System.out.println("    -> simpleExpressionStroke");
        simpleExpressionStroke();
    }

    private void simpleExpressionStroke() throws Exception {
        if (inFirst("op_sum")) {
            System.out.println("    -> op_sum");
            opSum();
            System.out.println("    -> term");
            term();
            System.out.println("    -> simpleExpressionStroke");
            simpleExpressionStroke();
        }
    }

    private void term() throws Exception {
        System.out.println("        -> factor");
        factor();
        System.out.println("        -> termStroke");
        termStroke();
    }

    private void termStroke() throws Exception {
        if (inFirst("op_mult")) {
            System.out.println("    -> op_mult");
            opMult();
            System.out.println("        -> factor");
            factor();
            System.out.println("        -> termStroke");
            termStroke();
        }
    }

    private void factor() throws Exception {
        if (inFirst("identifier")) {
            System.out.println("            -> identifier");
            identifier();
        } else if (inFirst("constant")) {
            System.out.println("            -> identifier");
            constant();
        } else if (inFirst("simple_expression_brackets")) {
            System.out.println("            -> simple_expression_brackets");
            simpleExpressionBrackets();
        } else if (inFirst("not_factor")) {
            System.out.println("            -> not_factor");
            notFactor();
            System.out.println("            -> factor");
            factor();
        } else
            throw new Exception(String.format("Unexpected symbol in input string, position=%d", i));
    }

    private void simpleExpressionBrackets() throws Exception {
        subStrStartWithTerm("(");
        addToStack("(");
        simpleExpression();
        subStrStartWithTerm(")");
        addToStack(")");
    }

    private void opRelationship() throws Exception {
        subStrStartWithTerm("<>", "<=", ">=", "<", "=", ">");
        addToStack("<>", "<=", ">=", "<", "=", ">");
    }

    private void opSum() throws Exception {
        subStrStartWithTerm("+", "-", "0R");
        addToStack("+", "-", "0R");
    }

    private void opMult() throws Exception {
        subStrStartWithTerm("*", "/", "DIV", "MOD", "AND");
        addToStack("*", "/", "DIV", "MOD", "AND");
    }

    protected void identifier() throws Exception {
        subStrStartWithTerm("i");
        addToPostfix("i");
    }

    private void constant() throws Exception {
        subStrStartWithTerm("c");
        addToPostfix("c");
    }

    private void notFactor() throws Exception {
        subStrStartWithTerm("NOT");
        addToStack("NOT");
    }

    private void sign() throws Exception {
        subStrStartWithTerm("+", "-");
        addToStack("+", "-");
    }

    private void addToPostfix(String... items) {
        for (String item : items) {
            if (item.equals(current)) {
                postfix = new StringBuffer(postfix).append(current).toString();
            }
        }
    }

    private void addToStack(String... items) {
        for (String item : items) {
            if (item.equals(current)) {
                if (item.equals("=")) {
                    postfix = new StringBuffer(postfix).append(item).toString();
                } else if (item.equals(")")) {
                    while (!stack.isEmpty()) {
                        String t = stack.pop();
                        if (!t.equals("(")) {
                            postfix = new StringBuffer(postfix).append(t).toString();
                        } else {
                            pushToPostfix();
                        }
                    }
                } else {
                    pushToPostfix();
                    stack.push(current);
                }
            }
        }
    }

    private void pushToPostfix() {
        while (!stack.isEmpty() && hasLowerPrecedence(current, stack.peek())) {
            if (!stack.peek().equals("(")) {
                postfix = new StringBuffer(postfix).append(stack.pop()).toString();
            } else {
                break;
            }
        }
    }

    private boolean hasLowerPrecedence(String operator1, String operator2) {
        return precedence(operator1) <= precedence(operator2);
    }

    private int precedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 3;
        };
    }
}

