package pt.isel.poo.li21d.g14.snake.model.Cells;


import pt.isel.poo.li21d.g14.snake.model.*;


import java.util.LinkedList;

public class Head extends Cell {
    public static int addnrtails;
    public int nrsteps;
    public int lastl,lastc;
    public int tailpremovelast_l,tailpremovelast_c;

    private LinkedList<Cell> snakebody =new LinkedList<>();

    public Head(Dir dir){
        super();
        nrsteps=0;
        addnrtails=4;
        this.dir= dir;
    }

    @Override
    public String toString(){ return "Head"; }

    @Override
    public char getChar(){ return '@'; }

    public void move() {
        nrsteps++;

        //decrementar a snake passado o nrsteps
        if (nrsteps==10){
           Game.setScore(Game.getScore()-1);
           Game.scoreUpdated(Game.getScore());
           if(snakebody.size()==0)
              snakeisdead=true;
           else {
                Cell toremove=snakebody.pollLast();
                removeCell(toremove.posl,toremove.posc);
                Level.cellRemoved(toremove.posl,toremove.posc);
                nrsteps=0;
           }
        }

        int nposl=this.posl+this.dir.dLine;
        if(nposl<0)nposl=Level.height-1;
        else if(nposl>Level.height-1)nposl=0;
        int nposc=this.posc+this.dir.dCol ;
        if(nposc<0)nposc=Level.width-1;
        else if(nposc>Level.width-1)nposc=0;


        Cell colide=Level.getCell(nposl,nposc);
        if(colide!=null){
        switch (colide.getChar()){
            case 'X':
            case '*':
            case '#':
                snakeisdead=true;break;
            case 'M':
                Game.setScore(Game.getScore()+ Mouse.points);
                Game.scoreUpdated(Game.getScore());
                addnrtails+=10;
                break;
            case 'A':
                Apple.gotEaten();
                Game.setScore(Game.getScore()+ Apple.points);
                Game.scoreUpdated(Game.getScore());
                addnrtails+=4;
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
            if(addnrtails>0){
                Cell tail= Cell.newInstance('#');
                if(snakebody.size()==0){
                    Level.putCell(lastl,lastc,tail);
                    Level.cellCreated(lastl,lastc,tail);
                }
                else {
                    Level.putCell(tailpremovelast_l,tailpremovelast_c,tail);
                    Level.cellCreated(tailpremovelast_l,tailpremovelast_c,tail);
                }
                --addnrtails;
                snakebody.add(tail);
            }
            this.posl=nposl;
            this.posc=nposc;
            moveCell(this,this.posl,this.posc);
        }
    }
}

