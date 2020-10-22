package pt.isel.poo.li21d.g14.snake.model.Cells;

import pt.isel.poo.li21d.g14.snake.model.Cell;
import pt.isel.poo.li21d.g14.snake.model.Dir;
import pt.isel.poo.li21d.g14.snake.model.Level;

public class Mouse extends Cell {
    public static final int points = 10;
    public  int nrsteps=0;
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
        nrsteps++;
        if(nrsteps>=4){
            nrsteps=0;
            int l,c;
            do {Dir movenext=Dir.getRandom();
                l =posl+movenext.dLine;
                c = posc+movenext.dCol;
                if(l<0)l=Level.height-1;
                else if(l>Level.height-1)l=0;
                if(c<0)c=Level.width-1;
                else if(c>Level.width-1)c=0;
            } while(Level.map[l][c]!=null);
            Level.cellMoved(this.posl,this.posc,l,c,this);
            removeCell(posl,posc);
            posl=l;
            posc=c;
            moveCell(this,this.posl,this.posc);
        }

    }
}
