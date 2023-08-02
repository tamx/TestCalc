package jp.cane.tam.calc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class calc {
    public static int calc_sub(parser.Syntax sytx) {
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
        return 0;
    }

    public static int Exec(String command) throws Exception {
        lexer.Word[] sentence = lexer.Lexer(command + "\n");
        if (sentence == null) {
            throw new Exception();
        }
        if (sentence.length == 1 && sentence[0].Type == lexer.TYPE_OTHERS) {
            throw new Exception();
        }
        parser.Syntax sytx = parser.Parser(null, sentence, 0);
        if (sytx.Cost != sentence.length - 1) {
            System.out.println(sytx.Cost);
            System.out.println("Error");
            throw new Exception();
        }
        int result = calc_sub(sytx);
        return result;
    }

    public static void main(String argv[]) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (true) {
            String line = reader.readLine();
            System.out.println(line);
            int result = Exec(line);
            System.out.println(result);
        }
    }
}