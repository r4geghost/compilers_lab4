package ru.dyusov;

public class CParser extends Parser {
    public CParser() {
        first.put("tail_not_empty", ";");
    }

    public void run() throws Exception {
        program();
    }

    private void program() throws Exception {
        block();
    }

    private void block() throws Exception {
        subStrStartWithTerm("{");
        listParams();
        subStrStartWithTerm("}");
    }

    private void listParams() throws Exception {
        operator();
        tail();
    }

    private void tail() throws Exception {
        System.out.println("    -> tail_not_empty");
        if (inFirst("tail_not_empty"))
            tail_notEmpty();
    }

    private void tail_notEmpty() throws Exception {
        subStrStartWithTerm(";");
        operator();
        tail();
    }

    private void operator() throws Exception {
        identifier();
        subStrStartWithTerm("=");
        expression();
    }
}
