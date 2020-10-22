package pt.isel.poo.li21d.g14.snake.model.Cells;

import pt.isel.poo.li21d.g14.snake.model.Cell;

public class Wall extends Cell {
    public Wall(){
        super();
    }

    public String toString(){
        return "Wall";
    }

    public char getChar(){
        return 'X';
    }
}
