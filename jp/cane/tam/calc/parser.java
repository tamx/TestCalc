package jp.cane.tam.calc;

public class parser {
    /*
     * S := TERM | S + TERM | S - TERM
     * TERM := PRODUCT | TERM * PRODUCT | TERM / PRODUCT
     * PRODUCT := NUMBER | ( S )
     */

    public static class Syntax {
        lexer.Word Leaf;
        Syntax[] Child;
        int Cost = 0;

        public Syntax(lexer.Word leaf, Syntax[] child, int cost) {
            this.Leaf = leaf;
            this.Child = child;
            this.Cost = cost;
        }
    }

    public static Syntax parser_product(lexer.Word[] sentence,
            int index) {
        lexer.Word s = sentence[index];
        if (s.Type == lexer.TYPE_NUMBER) {
            Syntax sytx = new Syntax(s, null, 1);
            return sytx;
        }
        if (s.Type == lexer.TYPE_SYMBOL && s.Value.equals("(")) {
            Syntax sytx = Parser(null, sentence, index + 1);
            lexer.Word end = sentence[index + sytx.Cost + 1];
            if (end.Type == lexer.TYPE_SYMBOL &&
                    end.Value.equals(")")) {
                sytx.Cost += 2;
                return sytx;
            }
        }
        System.out.println(s.Value);
        return null;
    }

    public static Syntax parser_term(Syntax s1,
            lexer.Word[] sentence,
            int index) {
        int length1 = 0;
        if (s1 == null) {
            s1 = parser_product(sentence, index);
            length1 = s1.Cost;
        }
        if (index + length1 >= sentence.length) {
            return s1;
        }
        lexer.Word s = sentence[index + length1];
        if (s.Type == lexer.TYPE_SYMBOL &&
                (s.Value.equals("*") || s.Value.equals("/"))) {
            Syntax s2 = parser_product(sentence, index + length1 + 1);
            Syntax sytxThis = new Syntax(
                    s,
                    new Syntax[] {
                            s1, s2 },
                    s1.Cost + 1 + s2.Cost);
            Syntax sytx = parser_term(sytxThis,
                    sentence, index + sytxThis.Cost);
            return sytx;
        }
        return s1;
    }

    public static Syntax Parser(Syntax s1,
            lexer.Word[] sentence,
            int index) {
        if (sentence == null || sentence.length == 0) {
            return null;
        }
        int length1 = 0;
        if (s1 == null) {
            s1 = parser_term(null, sentence, index);
            length1 = s1.Cost;
        }
        if (length1 + index >= sentence.length) {
            return s1;
        }
        lexer.Word s = sentence[index + length1];
        if (s.Type == lexer.TYPE_SYMBOL &&
                (s.Value.equals("+") || s.Value.equals("-"))) {
            Syntax s2 = parser_term(null,
                    sentence, index + length1 + 1);
            // int length2 = s2.Cost;
            Syntax sytxThis = new Syntax(
                    s,
                    new Syntax[] { s1, s2 },
                    s1.Cost + 1 + s2.Cost);
            Syntax sytx = Parser(sytxThis,
                    sentence, index + sytxThis.Cost);
            return sytx;
        }
        return s1;
    }
}
