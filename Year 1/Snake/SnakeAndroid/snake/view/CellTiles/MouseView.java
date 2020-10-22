package pt.isel.poo.li21d.g14.snake.view.CellTiles;


import android.graphics.Canvas;

import pt.isel.poo.li21d.g14.snake.R;
import pt.isel.poo.li21d.g14.snake.view.CellTileView;
import pt.isel.poo.li21d.g14.tile.Img;

public class MouseView extends CellTileView {

    @Override
    public void draw(Canvas canvas, int side) {
        Img ig = new Img(ctx, R.drawable.mouse);
        ig.draw(canvas,side,side,paint);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
