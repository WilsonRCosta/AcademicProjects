package isel.cn;

public class Task {
    private final String filename;
    private final String sessionId;
    private final String text;

    public Task(String filename, String text) {
        this.filename = filename;
        this.sessionId = filename.split("-")[0];
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getText() {
        return text;
    }
}
