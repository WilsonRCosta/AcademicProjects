package isel.poo.snake.model.Cells;

import isel.poo.snake.model.Cell;

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
