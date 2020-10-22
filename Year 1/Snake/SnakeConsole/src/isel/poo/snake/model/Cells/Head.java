package isel.poo.snake.model.Cells;


import isel.poo.snake.model.Cell;
import isel.poo.snake.model.Dir;
import isel.poo.snake.model.Game;
import isel.poo.snake.model.Level;

public class Head extends Cell {
    private static final int MAXSTEPS = 10;
    private int nrSteps;

    public Head(Dir dir){
        super();
        nrSteps = 0;
        this.addTails = 4;
        this.dir = dir;
    }
    public Head(){}

    void setNrTails(int numTails){ this.addTails += numTails; }

    @Override
    public String toString(){ return "Head"; }

    @Override
    public char getChar(){ return '@'; }

    public void move() {
        if (nrSteps++ == MAXSTEPS) decSnakeSize();

        int nPosL = Level.checkLin(this.pLine + this.dir.dLine);
        int nPosC = Level.checkCol(this.pCol + this.dir.dCol);

        if(!collide(nPosL,nPosC)) moveTails(this,this.snakeBody,nPosL,nPosC);
    }

    private void decSnakeSize(){
        Game.setScore(Game.getScore()-1);
        Game.scoreUpdated(Game.getScore());
        if(this.snakeBody.size()==0)
            snakeIsDead = true;
        else {
            Cell toRemove = this.snakeBody.pollLast();
            removeCell(toRemove.pLine,toRemove.pCol);
            Level.cellRemoved(toRemove.pLine,toRemove.pCol);
            nrSteps = 0;
        }
    }

    private boolean collide(int nPosL, int nPosC){
        Cell common = Level.getCell(nPosL,nPosC);
        if(common != null) {
            switch (common.getChar()){
                case 'X':
                case '*':
                case '#':
                    snakeIsDead = true; break;
                case 'M':
                    Game.setScore(Game.getScore()+ Mouse.points);
                    Game.scoreUpdated(Game.getScore());
                    setNrTails(10);
                    break;
                case 'A':
                    Apple.gotEaten();
                    Game.setScore(Game.getScore() + Apple.points);
                    Game.scoreUpdated(Game.getScore());
                    setNrTails(4);
                    break;
            }
        }
        Game.cellUpdated(this.pLine, this.pCol, this);
        return snakeIsDead;
    }
}
