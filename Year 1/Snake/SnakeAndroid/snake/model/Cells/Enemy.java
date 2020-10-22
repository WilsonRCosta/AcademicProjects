package pt.isel.poo.li21d.g14.snake.model.Cells;

import pt.isel.poo.li21d.g14.snake.model.Cell;
import pt.isel.poo.li21d.g14.snake.model.Dir;
import pt.isel.poo.li21d.g14.snake.model.Game;
import pt.isel.poo.li21d.g14.snake.model.Level;

import java.util.LinkedList;

public class Enemy extends Cell {
    public int lastl,lastc;
    public int tailpremovelast_l,tailpremovelast_c;
    private LinkedList<Cell> snakebody = new LinkedList<>();
    public int enemyaddnrtails;
    private boolean enemyIsDead = false;

    public Enemy(){
            super();
            enemyaddnrtails = 4;
        }

        public String toString(){
            return "Enemy";
        }

        public char getChar(){ return '*'; }

    @Override
    public void move() {
        int nposl=0,nposc=0;

        for (Dir dir : Dir.values()) {
            nposl=this.posl+dir.dLine;
            if(nposl<0)
                nposl= Level.height-1;
            else if(nposl>Level.height-1)
                nposl=0;
            nposc=this.posc+dir.dCol ;
            if(nposc<0)
                nposc=Level.width-1;
            else if(nposc>Level.width-1)
                nposc=0;
            if(Level.map[nposl][nposc]==null|| Level.map[nposl][nposc]instanceof Mouse ||Level.map[nposl][nposc]instanceof Apple)break;
        }

        Cell colide=Level.getCell(nposl,nposc);
        if(colide!=null) {
            switch (colide.getChar()){
                case 'A':
                    Apple.remainingApples++;
                    Apple.gotEaten();
                    enemyaddnrtails+=4;
                    break;
                case '@':
                    Head.addnrtails+=snakebody.size();
                    snakeisdead=true;
                    break;
                case '#':
                    Head.addnrtails+=snakebody.size();
                    snakeisdead=true;
                    break;
                case 'M':
                    enemyaddnrtails+=10;
                    break;
                case 'X':
                    snakeisdead=true;
                    break;
            }
        }

        Game.cellUpdated(this.posl, this.posc, this);
        if(!snakeisdead){
            Level.cellMoved(this.posl,this.posc,nposl,nposc,this);
            removeCell(this.posl,this.posc);
            lastl=this.posl;
            lastc=this.posc;

            if(snakebody.size()>0){
                Cell lasttail=snakebody.pollLast();
                removeCell(lasttail.posl,lasttail.posc);
                moveCell(lasttail,lastl,lastc);
                Level.cellMoved(lasttail.posl,lasttail.posc,lastl,lastc,lasttail);
                tailpremovelast_l = lasttail.posl;
                tailpremovelast_c=  lasttail.posc;
                lasttail.posc=lastc;
                lasttail.posl=posl;
                snakebody.addFirst(lasttail);
            }
            if(enemyaddnrtails>0){
                Cell tail= Cell.newInstance('#');
                if(snakebody.size()==0){
                    Level.putCell(lastl,lastc,tail);
                    Level.cellCreated(lastl,lastc,tail);
                }else{
                    Level.putCell(tailpremovelast_l,tailpremovelast_c,tail);
                    Level.cellCreated(tailpremovelast_l,tailpremovelast_c,tail);
                }
                --enemyaddnrtails;
                snakebody.add(tail);
            }
            this.posl=nposl;
            this.posc=nposc;
            moveCell(this,this.posl,this.posc);
        }
        else if(this.snakebody.size()>0 ){
            Cell elemtail=snakebody.pollLast();
            removeCell(elemtail.posl,elemtail.posc);
            Level.cellRemoved(elemtail.posl,elemtail.posc);
        }
    }
}
