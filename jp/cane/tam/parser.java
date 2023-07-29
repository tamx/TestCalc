public class parser {
    /*
     * S := TERM | TERM + S | TERM - S
     * TERM := PRODUCT | PRODUCT * TERM | PRODUCT / TERM
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

    public static Syntax parser_product(lexer.Word[] sentence, int index) {
        lexer.Word s = sentence[index];
        if (s.Type == lexer.TYPE_NUMBER) {
            Syntax sytx = new Syntax(s, null, 1);
            return sytx;
        }
        if (s.Type == lexer.TYPE_SYMBOL && s.Value.equals("(")) {
            Syntax sytx = Parser(sentence, index + 1);
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

    public static Syntax parser_term(lexer.Word[] sentence, int index) {
        Syntax s1 = parser_product(sentence, index);
        int length1 = s1.Cost;
        if (length1 >= sentence.length) {
            return s1;
        }
        lexer.Word s = sentence[index + length1];
        if (s.Type == lexer.TYPE_SYMBOL &&
                (s.Value.equals("*") || s.Value.equals("/"))) {
            Syntax s2 = parser_term(sentence, index + length1 + 1);
            int length2 = s2.Cost;
            Syntax sytx = new Syntax(
                    s,
                    new Syntax[] {
                            s1, s2 },
                    length1 + 1 + length2);
            return sytx;
        }
        return s1;
    }

    public static Syntax Parser(lexer.Word[] sentence, int index) {
        if (sentence == null || sentence.length == 0) {
            return null;
        }
        Syntax s1 = parser_term(sentence, index);
        int length1 = s1.Cost;
        if (length1 >= sentence.length) {
            return s1;
        }
        lexer.Word s = sentence[index + length1];
        if (s.Type == lexer.TYPE_SYMBOL &&
                (s.Value.equals("+") || s.Value.equals("-"))) {
            Syntax s2 = Parser(sentence, index + length1 + 1);
            int length2 = s2.Cost;
            Syntax sytx = new Syntax(
                    s,
                    new Syntax[] { s1, s2 },
                    length1 + 1 + length2);
            return sytx;
        }
        return s1;
    }
}
