package pt.isel.poo.li21d.g14.snake.model.Cells;

import pt.isel.poo.li21d.g14.snake.model.Cell;
import pt.isel.poo.li21d.g14.snake.model.Game;
import pt.isel.poo.li21d.g14.snake.model.Level;


public class Apple extends Cell {
    public static final int points = 4;
    public static int initApples;
    public static int remainingApples;

    public Apple(){ super(); }
    public String toString(){ return "Apple"; }

    public char getChar(){ return 'A'; }

    public static void gotEaten() {

        remainingApples--;
        if (remainingApples >= initApples){

                int l, c;
                do {
                    l = (int) (Math.random()*Level.height);
                    c = (int) (Math.random()*Level.width);
                } while(!cellIsEmpty(l,c));

                Cell newApple = Cell.newInstance('A');
                Level.putCell(l,c,newApple);
                Level.cellCreated(l,c,newApple);
                initApples--;
        }
        Game.applesUpdated(remainingApples);
    }
}
