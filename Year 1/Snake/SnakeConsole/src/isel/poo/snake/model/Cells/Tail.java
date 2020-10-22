package isel.poo.snake.model.Cells;

import isel.poo.snake.model.Cell;

public class Tail extends Cell {
    public Tail(){
        super();
    }

    public String toString(){
        return "Tail";
    }

    public char getChar(){
        return '#';
    }
}
