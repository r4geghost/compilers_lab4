package ru.dyusov;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        System.out.println("Default parser:");
//        System.out.println(parser.eval("i=NOTc"));
//        System.out.println(parser.eval("i/c"));
//        System.out.println(parser.eval("cMODc"));
//        System.out.println(parser.eval("c=-i"));
//        System.out.println(parser.eval("i>=c"));
//        System.out.println(parser.eval("iDIVc"));
//        System.out.println(parser.eval("c+(c*c)"));

//        System.out.println(parser.eval("cc"));
//        System.out.println(parser.eval("i)"));
//        System.out.println(parser.eval("ic"));
//        System.out.println(parser.eval("i=i>=i*i*(C+i)"));

//        System.out.println(parser.eval("X"));

        CParser cParser = new CParser();
        System.out.println("CParser parser:");

        System.out.println("{i=-c}:");
        cParser.eval("{i=-c}");
//
//
        System.out.println("{i=c;i=c/i}:");
        cParser.eval("{i=c;i=c/i}");
//
//
//        System.out.println("{i=cANDi}:");
//        cParser.eval("{i=cANDi}");
//        System.out.println(cParser.eval("{i=cANDi}"));

//        System.out.println(cParser.eval("i=c"));
//        System.out.println(cParser.eval("{cc}"));
//        System.out.println(cParser.eval("{i=(c}"));
    }
}