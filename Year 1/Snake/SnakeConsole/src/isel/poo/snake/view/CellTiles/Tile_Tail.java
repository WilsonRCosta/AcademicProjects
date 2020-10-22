package isel.poo.snake.view.CellTiles;

import isel.leic.pg.Console;
import isel.poo.console.tile.Tile;
import isel.poo.snake.model.Cell;
import isel.poo.snake.view.CellTile;

public class Tile_Tail extends CellTile {
    public Tile_Tail(Cell cell){
        super(cell);
    }
    public void paint() {
        Console.color(Console.BLACK,Console.RED);
        print(0,0, "#");
    }
}
