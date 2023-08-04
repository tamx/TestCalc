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

    private lexer.Word[] sentence = null;

    public parser(lexer.Word[] sentence) {
        this.sentence = sentence;
    }

    private Syntax parse_product(int index) throws Exception {
        lexer.Word s = this.sentence[index];
        if (s.Type == lexer.TYPE_NUMBER) {
            Syntax sytx = new Syntax(s, null, 1);
            return sytx;
        }
        if (s.Type == lexer.TYPE_SYMBOL && s.Value.equals("(")) {
            Syntax sytx = parse(null, index + 1);
            lexer.Word end = this.sentence[index + sytx.Cost + 1];
            if (end.Type == lexer.TYPE_SYMBOL &&
                    end.Value.equals(")")) {
                sytx.Cost += 2;
                return sytx;
            }
        }
        System.out.println("Syntax error: " + s.Value);
        throw new Exception("Syntax error: " + s.Value);
    }

    private Syntax parse_term(Syntax s1, int index) throws Exception {
        int length1 = 0;
        if (s1 == null) {
            s1 = parse_product(index);
            length1 = s1.Cost;
        }
        if (index + length1 >= this.sentence.length) {
            return s1;
        }
        lexer.Word s = this.sentence[index + length1];
        if (s.Type == lexer.TYPE_SYMBOL &&
                (s.Value.equals("*") || s.Value.equals("/"))) {
            Syntax s2 = parse_product(index + length1 + 1);
            Syntax sytxThis = new Syntax(
                    s,
                    new Syntax[] {
                            s1, s2 },
                    s1.Cost + 1 + s2.Cost);
            Syntax sytx = parse_term(sytxThis,
                    index + length1 + 1 + s2.Cost);
            return sytx;
        }
        return s1;
    }

    private Syntax parse(Syntax s1, int index) throws Exception {
        if (sentence == null || sentence.length == 0) {
            return null;
        }
        int length1 = 0;
        if (s1 == null) {
            s1 = parse_term(null, index);
            length1 = s1.Cost;
        }
        if (length1 + index >= sentence.length) {
            return s1;
        }
        lexer.Word s = sentence[index + length1];
        if (s.Type == lexer.TYPE_SYMBOL &&
                (s.Value.equals("+") || s.Value.equals("-"))) {
            Syntax s2 = parse_term(null,
                    index + length1 + 1);
            // int length2 = s2.Cost;
            Syntax sytxThis = new Syntax(
                    s,
                    new Syntax[] { s1, s2 },
                    s1.Cost + 1 + s2.Cost);
            Syntax sytx = parse(sytxThis,
                    index + length1 + 1 + s2.Cost);
            return sytx;
        }
        return s1;
    }

    public Syntax Parse() throws Exception {
        return parse(null, 0);
    }
}
