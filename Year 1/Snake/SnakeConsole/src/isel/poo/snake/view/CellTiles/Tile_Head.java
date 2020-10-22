package isel.poo.snake.view.CellTiles;

import isel.leic.pg.Console;
import isel.poo.snake.model.Cell;
import isel.poo.snake.view.CellTile;

public class Tile_Head extends CellTile {

    public Tile_Head(Cell cell){
        super(cell);
    }

    public void paint() {
        Console.color(Console.BLACK,Console.YELLOW);
        if(!cell.snakeIsDead)
            print(0,0, "@");
        else
            print(0,0, "X");

    }

}
