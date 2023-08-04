package jp.cane.tam.calc;

import java.util.ArrayList;

public class lexer {
    public static int TYPE_SPACE = 0;
    public static int TYPE_NUMBER = 1;
    public static int TYPE_SYMBOL = 2;
    public static int TYPE_OTHERS = -1;
    private static String SYMBOL_CODES = "+-*/()";

    public static class Word {
        public int Type;
        public String Value;

        public Word(int type, String value) {
            this.Type = type;
            this.Value = value;
        }
    }

    private String line = "";

    public lexer(String line) {
        this.line = line + "\n";
    }

    private Word lexer_analyze(int index) {
        for (int i = index; i < this.line.length(); i++) {
            char r = this.line.charAt(i);
            if (r == ' ' || r == '\t') {
                continue;
            }
            if (i > index) {
                Word w = new Word(TYPE_SPACE,
                        this.line.substring(index, i));
                return w;
            }
            break;
        }
        for (int i = index; i < this.line.length(); i++) {
            char r = this.line.charAt(i);
            if (r >= '0' && r <= '9') {
                continue;
            }
            if (i > index) {
                Word w = new Word(TYPE_NUMBER,
                        this.line.substring(index, i));
                return w;
            }
            break;
        }
        if (SYMBOL_CODES.indexOf(this.line.charAt(index)) >= 0) {
            Word w = new Word(TYPE_SYMBOL,
                    this.line.substring(index, index + 1));
            return w;
        }
        Word w = new Word(TYPE_OTHERS,
                this.line.substring(index, index + 1));
        return w;
    }

    public Word[] analyze() throws Exception {
        ArrayList<Word> sentence = new ArrayList<Word>();
        for (int index = 0; index < this.line.length();) {
            Word w = lexer_analyze(index);
            if (w.Type != TYPE_SPACE) {
                sentence.add(w);
            }
            if (w.Value.length() == 0) {
                System.out.println("Error: lexer analyze detected the 0 length word.");
                throw new Exception("0 length word");
            }
            index += w.Value.length();
        }
        return sentence.toArray(new Word[0]);
    }
}