import java.io.BufferedReader;
import java.io.IOException;
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

    public static void main(String argv[]) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (true) {
            String line = reader.readLine();
            System.out.println(line);
            lexer.Word[] sentence = lexer.Lexer(line + "\n");
            if (sentence == null) {
                return;
            }
            if (sentence.length == 1 && sentence[0].Type == lexer.TYPE_OTHERS) {
                continue;
            }
            parser.Syntax sytx = parser.Parser(sentence, 0);
            if (sytx.Cost != sentence.length - 1) {
                System.out.println("Error");
                continue;
            }
            int result = calc_sub(sytx);
            System.out.println(result);
        }
    }
}