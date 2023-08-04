package jp.cane.tam.calc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class calc {
    public static int calc_sub(parser.Syntax sytx) throws Exception {
        lexer.Word leaf = sytx.Leaf;
        if (leaf.Type == lexer.TYPE_NUMBER) {
            int value = Integer.valueOf(leaf.Value);
            return value;
        }
        if (leaf.Type == lexer.TYPE_SYMBOL) {
            switch (leaf.Value) {
                case "+":
                    return calc_sub(sytx.Child[0]) + calc_sub(sytx.Child[1]);
                case "-":
                    return calc_sub(sytx.Child[0]) - calc_sub(sytx.Child[1]);
                case "*":
                    return calc_sub(sytx.Child[0]) * calc_sub(sytx.Child[1]);
                case "/":
                    return calc_sub(sytx.Child[0]) / calc_sub(sytx.Child[1]);
            }
        }
        System.out.println("something wrong");
        throw new Exception("something wrong");
    }

    public static int Exec(String command) throws Exception {
        lexer.Word[] sentence = new lexer(command).analyze();
        if (sentence == null) {
            throw new Exception();
        }
        if (sentence.length == 1 && sentence[0].Type == lexer.TYPE_OTHERS) {
            throw new Exception();
        }
        parser.Syntax sytx = new parser(sentence).Parse();
        if (sytx.Cost != sentence.length - 1) {
            System.out.println("Error: Syntax cost is " + sytx.Cost);
            throw new Exception();
        }
        int result = calc_sub(sytx);
        return result;
    }

    private static void test(int expectedValue, String equation) {
        try {
            int result = Exec(equation);
            if (result != expectedValue) {
                System.err.println("NG: " + equation);
                System.err.println("expected is " + expectedValue +
                        " but " + result);
                return;
            }
            System.err.println("OK: " + equation + " = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CUI(String argv[]) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (true) {
            String line = reader.readLine();
            System.out.println(line);
            int result = Exec(line);
            System.out.println(result);
        }
    }

    public static void main(String argv[]) {
        test(3, "3");
        test(6543, "6543");
        test(3, "003");
        test(8, "3+5");
        test(-7, "3-5-5");
        test(15, "3*5");
        test(4, "8/2");
        test(12, "8/2*3");
        test(7, "3+8/2");
        test(1, "3-8/2+2");
        test(-5, "3-5-5+2");
        test(2, "6/3*2*2/4");
        test(6, "(3+9)/2");
        test(1, "3-8/(2+2)");
    }
}