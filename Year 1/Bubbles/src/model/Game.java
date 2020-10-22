package model;

import controller.Bubbles;
import view.Panel;

import static isel.leic.pg.Console.sleep;

public class Game {

    public static int[][] colorValues = new int[Bubbles.LINES][Bubbles.COLS];

    public static boolean[][] existBubble = new boolean[Bubbles.LINES][Bubbles.COLS];

    public static int countGroup, totalPoints;

    public static void saveColor(int l, int c, int position) { colorValues[l][c] = position; }

    public static void deleteGroup() {
        for (int l = 0; l < Bubbles.LINES; ++l)
            for (int c = 0; c < Bubbles.COLS; ++c)
                if (existBubble[l][c]) {
                    existBubble[l][c] = false;
                    Panel.drawBubble( l, c, colorValues[l][c], (l == Bubbles.cursorLin && c == Bubbles.cursorCol),false);
                }
    }

    public static void blowBubbles(boolean[][] existBubble, int[][] colorValues){
        for(int l = 0; l < Bubbles.LINES; ++l) {
            for(int c = 0; c < Bubbles.COLS; ++c) {
                if (existBubble[l][c]) {
                    Panel.drawBubble(l, c, 0, false, false);
                    colorValues[l][c] = 0;
                }
            }
        }
        dropBubbles(colorValues);
        while(shiftBubbles()) sleep(20);
        getTotalPoints();
    }

    public static void dropBubbles(int[][] bubble) {
        for (int lin1 = Bubbles.LINES - 1; lin1 >= 0; --lin1)
            for (int lin2 = Bubbles.LINES - 1; lin2 >= 0; --lin2)
                for (int col = Bubbles.COLS - 1 ; col >= 0 ; --col)
                    if (bubble[lin1][col] == 0 && bubble[lin1][col] < bubble[lin2][col] && lin1 > lin2) {
                        int change = bubble[lin2][col];
                        bubble[lin2][col] = bubble[lin1][col];
                        bubble[lin1][col] = change;
                        Panel.drawBubble(lin1, col, colorValues[lin1][col], false, false);
                        Panel.drawBubble(lin2, col, colorValues[lin2][col], false, false);
                        sleep(20);
                    }
    }

    public static boolean shiftBubbles() {
        for (int c = 0; c < Bubbles.COLS - 1; ++c)
            if (colorValues[Bubbles.LINES-1][c+1] == 0 && colorValues[Bubbles.LINES-1][c] != 0) {
                for (int l = 0; l < Bubbles.LINES; ++l)
                    if (colorValues[l][c] != 0) {
                        colorValues[l][c + 1] = colorValues[l][c];
                        colorValues[l][c] = 0;
                        Panel.drawBubble(l, c, 0,(l == Bubbles.cursorLin && c == Bubbles.cursorCol), false);
                        Panel.drawBubble(l,c + 1, colorValues[l][c+1],(l == Bubbles.cursorLin && (c+1)== Bubbles.cursorCol), false);
                        sleep(5);
                    }
                return true;
            }
        return false;
    }

    public static void selectGroup() {
        int l = Bubbles.cursorLin;
        int c = Bubbles.cursorCol;
        if (colorValues[l][c] == 0) return;

        existBubble[l][c] = true;
        countGroup = 1;
        selectSideGroup(l - 1, c);
        selectSideGroup(l + 1, c);
        selectSideGroup(l, c - 1);
        selectSideGroup(l, c + 1);
    }

    private static void selectSideGroup(int l, int c) {
        if(l < 0 || c < 0  || l >= Bubbles.LINES || c >= Bubbles.COLS) return;
        if (existBubble[l][c]) return;
        if (colorValues[l][c] != colorValues[Bubbles.cursorLin][Bubbles.cursorCol]) return;

        existBubble[l][c] = true;
        countGroup++;
        Panel.drawBubble(l, c, colorValues[l][c],false,true);
        selectSideGroup(l - 1,c);
        selectSideGroup(l + 1,c);
        selectSideGroup(l,c - 1);
        selectSideGroup(l,c + 1);
    }

    public static int getCountGroup(){
        int points = 0;
        for(int i = 1; i <= countGroup; ++i)
            points += i * Bubbles.level;
        Panel.updateGroup(points);
        return points;
    }

    public static void getTotalPoints(){
        totalPoints += getCountGroup();
        Panel.updateTotal(totalPoints);

    }
}