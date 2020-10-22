package isel.poo.snake.view;

import isel.leic.pg.Console;
import isel.poo.console.tile.Tile;

public class EmptyTile extends Tile {

    public void paint() {

        Console.color(Console.LIGHT_GRAY,Console.LIGHT_GRAY);
        print(0,0, " ");

    }
}
