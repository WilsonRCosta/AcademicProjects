package isel.poo.snake.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Game {
    private final InputStream input;
    private static int score = 0;
    private int levelNumber = 0;
    private Level curLevel = null;
    private static Listener listener = null;

    void addScore(int points) {
        score += points;
        if (listener!=null) listener.scoreUpdated(score);
    }

    // --- Methods to be use by Controller ---

    public Game(InputStream levelsFile) {
        input = levelsFile.markSupported() ? levelsFile : new BufferedInputStream(levelsFile);
        input.mark(40*1024);
    }

    public Level loadNextLevel() throws Loader.LevelFormatException {
        try {
            input.reset();
            Scanner in = new Scanner(input);
            curLevel = new Loader(in).load(++levelNumber);
            if (curLevel!=null) {
                curLevel.init(this);
            }
            return curLevel;
        } catch (IOException e) {
            throw new RuntimeException("IOException",e);
        }
    }

    public static int getScore() { return score; }
    public static void setScore(int newscore) { score=newscore; }
    public static void cellUpdated(int l, int c, Cell cell){
        if (listener!=null)
            listener.cellUpdated(l,c,cell);
    }
    public static void scoreUpdated(int score){
        if (listener!=null)
            listener.scoreUpdated(score);

    }
    public static void applesUpdated(int apples){
        if (listener!=null)
            listener.applesUpdated(apples);

    }
    public interface Listener {
        void scoreUpdated(int score);
        void cellUpdated(int l, int c, Cell cell);
        void applesUpdated(int apples);
    }
    public void setListener(Listener l) { listener = l; }
}
