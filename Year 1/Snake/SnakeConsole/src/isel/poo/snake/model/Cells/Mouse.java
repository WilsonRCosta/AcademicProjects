package isel.poo.snake.model.Cells;

import isel.poo.snake.model.Cell;
import isel.poo.snake.model.Dir;
import isel.poo.snake.model.Level;

public class Mouse extends Cell {
    static final int points = 10;
    private int nrSteps = 0;
    private static final int MAX_STEPS = 4;
    public Mouse(){
        super();
    }

    public String toString(){
        return "Mouse";
    }

    public char getChar(){
        return 'M';
    }

    public void move() {
        nrSteps++;
        if(nrSteps >= MAX_STEPS){
            nrSteps = 0;
            int l,c;
            do {Dir nextMove = Dir.getRandom();
                l = Level.checkLin(pLine +nextMove.dLine);
                c = Level.checkCol(pCol +nextMove.dCol);
            } while(!cellIsEmpty(l,c));
            Level.cellMoved(this.pLine,this.pCol,l,c,this);
            removeCell(pLine, pCol);
            pLine = l;
            pCol = c;
            moveCell(this,this.pLine,this.pCol);
        }
    }
}
