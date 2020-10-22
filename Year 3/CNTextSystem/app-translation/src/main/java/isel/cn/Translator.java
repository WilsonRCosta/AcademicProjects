package isel.cn;

public class Translator {
    private final String text;
    private final String targetLang;

    public Translator(String text, String targetLang) {
        this.text = text;
        this.targetLang = targetLang;
    }

    public String getText() {
        return text;
    }

    public String getTargetLang() {
        return targetLang;
    }
}
