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

    private static Word lexer_analyze(String line_sub) {
        for (int index = 0; index < line_sub.length(); index++) {
            char r = line_sub.charAt(index);
            if (r == ' ' || r == '\t') {
                continue;
            }
            if (index > 0) {
                Word w = new Word(TYPE_SPACE,
                        line_sub.substring(0, index));
                return w;
            }
            break;
        }
        for (int index = 0; index < line_sub.length(); index++) {
            char r = line_sub.charAt(index);
            if (r >= '0' && r <= '9') {
                continue;
            }
            if (index > 0) {
                Word w = new Word(TYPE_NUMBER,
                        line_sub.toString().substring(0, index));
                return w;
            }
            break;
        }
        if (SYMBOL_CODES.indexOf(line_sub.charAt(0)) >= 0) {
            Word w = new Word(TYPE_SYMBOL,
                    line_sub.substring(0, 1));
            return w;
        }
        Word w = new Word(TYPE_OTHERS,
                line_sub.substring(0, 1));
        return w;
    }

    public static Word[] Lexer(String line) {
        ArrayList<Word> sentence = new ArrayList<Word>();
        for (int index = 0; index < line.length();) {
            Word w = lexer_analyze(line.substring(index));
            if (w.Type != TYPE_SPACE) {
                sentence.add(w);
            }
            if (w.Value.length() == 0) {
                System.out.println("Error: lexer analyze detected the 0 length word.");
                return null;
            }
            index += w.Value.length();
        }
        return sentence.toArray(new Word[0]);
    }
}