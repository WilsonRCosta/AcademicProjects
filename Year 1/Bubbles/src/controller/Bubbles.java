package controller;

import isel.leic.pg.Console;
import model.Game;
import view.Panel;

import java.awt.event.KeyEvent;
import static isel.leic.pg.Console.isKeyPressed;
import static isel.leic.pg.Console.sleep;

public class Bubbles {
    public static final int LINES = 10, COLS = 10, MAX_COLORS = 8;
    public static int cursorLin, cursorCol;
    public static int level;
    public static boolean levelEnded;

    public static void main(String[] args) {
        Panel.open();
        play();
        Panel.printMessage("GAME OVER");
        Panel.close();
    }

    private static void play() {
        for (level = 1 ;level < MAX_COLORS ; ++level) {
            if (level > 1) Panel.printMessage("NEXT LEVEL "+level);
            startLevel(level);
            Panel.updateLevel(level);

            do {
                int key = Console.waitKeyPressed(0);
                if (key != Console.NO_KEY)
                    action(key);
                if (key == KeyEvent.VK_ESCAPE) return;
            } while (canPlay());
        }
    }

    private static void action(int key) {
        switch (key) {
            case KeyEvent.VK_DOWN: while(isKeyPressed(key))cursor(+1,0); break;
            case KeyEvent.VK_UP: while(isKeyPressed(key))cursor(-1,0);  break;
            case KeyEvent.VK_RIGHT: while(isKeyPressed(key))cursor(0,+1); break;
            case KeyEvent.VK_LEFT: while(isKeyPressed(key))cursor(0,-1); break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                Game.blowBubbles(Game.existBubble,Game.colorValues); break;
            case KeyEvent.VK_ESCAPE: break;
        }
    }

    public static void cursor(int dl, int dc) {
        int cl = cursorLin, cc = cursorCol;
        int l = cl + dl, c = cc + dc;
        if (aboveGameLimits(l, c)) return;
        cursorLin = l; cursorCol = c;

        Panel.drawBubble(cl,cc,Game.colorValues[cl][cc],false,false);
        Panel.drawBubble(l,c,Game.colorValues[l][c],true,false);
        Game.deleteGroup();
        Game.selectGroup();
        sleep(150);
    }

    private static boolean equalColor(int color, int l, int c){
        if(aboveGameLimits(l, c)) return false;
        return Game.colorValues[l][c] == color;
    }

    public static boolean canPlay() {
        for (int l = 0; l < LINES ; l++)
            for (int c = 0; c <COLS ; c++) {
                int color = Game.colorValues[l][c];
                if(color !=0 && (
                        equalColor(color,l-1,c)
                        || equalColor(color,l+1,c)
                        || equalColor(color,l,c-1)
                        || equalColor(color,l,c+1)))
                    return true;
            }
        return false;
    }

    private static void startLevel(int level) { //
        int keepColor;
        for (int l = 0; l < LINES; l++)
            for (int c = 0; c < COLS; c++) {
                keepColor = ((int) (Math.random() * (level + 1)) + 1);
                Game.saveColor(l, c, keepColor);
                Panel.drawBubble(l, c, keepColor, false, false);
            }
        levelEnded = false;
        Panel.drawBubble(cursorLin,cursorCol,Game.colorValues[cursorLin][cursorCol],true,false);
        Game.selectGroup();
    }

    private static boolean aboveGameLimits(int line, int col) {
        return line < 0 || line >= LINES || col < 0 || col >= COLS;
    }
}