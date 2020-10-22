package isel.poo.snake.view;

import isel.leic.pg.Console;
import isel.poo.console.FieldView;
import isel.poo.console.ParentView;


public class StatusPanel extends ParentView {

    public static final int HEIGHT = 10, WIDTH = 6;

    private FieldView level = new FieldView("Level",1,0,"---");
    private FieldView apples = new FieldView("Apple",4,0,"---");
    private FieldView score = new FieldView("Score",7,0,"---");
    public StatusPanel(int winWidth) {
        super(0,winWidth, HEIGHT, WIDTH, Console.BLACK);
        addView(level);
        addView(apples);
        addView(score);

    }
    public void paint() {
        clear();
        level.paint();
        apples.paint();
        score.paint();
    }
    public void setLevel(int level) {
        this.level.setValue(level);
    }

    public void setApples(int apples) {
        this.apples.setValue(apples);
    }

    public void setScore(int score) {
        this.score.setValue(score);

    }
}
