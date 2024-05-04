package ru.dyusov;

import java.util.HashMap;

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
                i += item.length();
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
        }
        else if (inFirst("constant")) {
            System.out.println("            -> identifier");
            constant();
        }
        else if (inFirst("simple_expression_brackets")) {
            System.out.println("            -> simple_expression_brackets");
            simpleExpressionBrackets();
        }
        else if (inFirst("not_factor")) {
            System.out.println("            -> not_factor");
            notFactor();
            System.out.println("            -> factor");
            factor();
        }
        else
            throw new Exception(String.format("Unexpected symbol in input string, position=%d", i));
    }

    private void simpleExpressionBrackets() throws Exception {
        subStrStartWithTerm("(");
        simpleExpression();
        subStrStartWithTerm(")");
    }

    private void opRelationship() throws Exception {
        subStrStartWithTerm("<>", "<=", ">=", "<", "=", ">");
    }

    private void opSum() throws Exception {
        subStrStartWithTerm("+", "-", "0R");
    }

    private void opMult() throws Exception {
        subStrStartWithTerm("*", "/", "DIV", "MOD", "AND");
    }

    protected void identifier() throws Exception {
        subStrStartWithTerm("i");
    }

    private void constant() throws Exception {
        subStrStartWithTerm("c");
    }

    private void notFactor() throws Exception {
        subStrStartWithTerm("NOT");
    }

    private void sign() throws Exception {
        subStrStartWithTerm("+", "-");
    }
}

