package isel.poo.snake.view.CellTiles;

import isel.leic.pg.Console;
import isel.poo.snake.model.Cell;
import isel.poo.snake.view.CellTile;

public class Tile_Mouse extends CellTile {

    public Tile_Mouse(Cell cell){
        super(cell);
    }

    public void paint() {
        Console.color(Console.BLUE,Console.LIGHT_GRAY);
        print(0,0, "M");
    }
}
