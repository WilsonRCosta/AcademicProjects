package pt.isel.poo.li21d.g14.snake.view.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;

import pt.isel.poo.li21d.g14.snake.view.CellTileView;

public class TailView extends CellTileView {

    @Override
    public void draw(Canvas canvas, int side) {
        paint.setColor(Color.RED);
        canvas.drawCircle(side/2,side/2,5*side/8,paint);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(side/2,side/2,side/3,paint);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}