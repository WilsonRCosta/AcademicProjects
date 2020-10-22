package pt.isel.poo.li21d.g14.snake;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.InputMismatchException;

import pt.isel.poo.li21d.g14.snake.model.Cell;
import pt.isel.poo.li21d.g14.snake.model.Dir;
import pt.isel.poo.li21d.g14.snake.model.Game;
import pt.isel.poo.li21d.g14.snake.model.Level;
import pt.isel.poo.li21d.g14.snake.model.Loader;
import pt.isel.poo.li21d.g14.snake.view.CellTileView;
import pt.isel.poo.li21d.g14.snake.view.CellTiles.EmptyTileView;
import pt.isel.poo.li21d.g14.tile.OnBeatListener;
import pt.isel.poo.li21d.g14.tile.OnTileTouchListener;
import pt.isel.poo.li21d.g14.tile.Tile;
import pt.isel.poo.li21d.g14.tile.TilePanel;

public class SnakeActivity extends AppCompatActivity implements OnBeatListener{
    private TilePanel panel;
    private TextView levelNr, applesNr, scoreNr, nextLevel, gameOver;
    private Button okButton;
    protected Level model;
    private Game game;
    private Updater updater;

    private boolean buttonFinish = false;
    private boolean buttonNextLvl = false;
    private int levelNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        levelNr = findViewById(R.id.levelNr);
        applesNr = findViewById(R.id.applesNr);
        scoreNr = findViewById(R.id.scoreNr);
        CellTileView.ctx = this;
        panel = findViewById(R.id.TilePanel);
        panel.setBackgroundColor(Color.LTGRAY);

        init();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonFinish) finish();
                else if(buttonNextLvl) {
                    buttonNextLvl = false;
                    okButton.setVisibility(View.INVISIBLE);
                    nextLevel.setVisibility(View.INVISIBLE);
                    playLevel();
                }
            }
        });
         loadFile();
    }

    private void init() {
        okButton = findViewById(R.id.okButton);
        okButton.setVisibility(View.INVISIBLE);
        nextLevel = findViewById(R.id.nextLevel);
        nextLevel.setVisibility(View.INVISIBLE);
        gameOver = findViewById(R.id.gameOver);
        gameOver.setVisibility(View.INVISIBLE);
    }

    private void loadFile() {
        try {
            InputStream file = getResources().openRawResource(R.raw.levels);
            game = new Game(file);
            updater = new Updater();
            game.setListener(updater);
            playLevel();
        } catch (InputMismatchException e) {
            Toast.makeText(this, "Error loading file = \"" + levelNr + "\"" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void playLevel() {
        try {
            model = game.loadNextLevel();
            levelNum++;
        } catch (Loader.LevelFormatException e) {
            Toast.makeText(this, e.getMessage() + " in file \""+ levelNr +"\"", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, " "+e.getLineNumber()+": "+e.getLine(), Toast.LENGTH_SHORT).show();
        }
        panel.setSize(model.getWidth(),model.getHeight());
        panel.setListener(new MyPanelListener());
        panel.setHeartbeatListener(500,this);
        levelNr.setText(Integer.toString(model.getNumber()));
        scoreNr.setText(Integer.toString(Game.getScore()));
        applesNr.setText(Integer.toString(model.getRemainingApples()));

        for (int l = 0; l < model.getHeight(); l++)                                // Create each tile for each cell
            for (int c = 0; c < model.getWidth(); c++) {
                Tile t = CellTileView.tileOf( Level.getCell(l,c));
                panel.setTile(c,l,t );
                t.setContext(getContext());
            }
        model.setObserver(updater);
    }

    private class Updater implements Game.Listener, Level.Observer {
        // Game.Listener
        @Override
        public void scoreUpdated(int score) { scoreNr.setText(Integer.toString(score)); }
        @Override
        public void applesUpdated(int apples) { applesNr.setText(Integer.toString(model.getRemainingApples())); }
        @Override
        public void cellUpdated(int l, int c, Cell cell) { panel.setTile(c,l,CellTileView.tileOf(cell));  }
        // Level.Listener
        @Override
        public void cellCreated(int l, int c, Cell cell) { panel.setTile(c,l,CellTileView.tileOf(cell)); }
        @Override
        public void cellRemoved(int l, int c) { panel.setTile(c,l,new EmptyTileView()); }
        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
            Tile tile = panel.getTile(fromC,fromL);
            assert !(tile == null);
            panel.setTile(toC,toL,tile);
            cellRemoved(fromL,fromC);
        }
    }

    private class MyPanelListener implements OnTileTouchListener {
        private int startX =-1, startY;
        @Override
        public boolean onClick(int xTile, int yTile) { return false; }

        @Override
        public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
            if (startX !=-1) return true;
            startX = xFrom; startY =yFrom;
            return true;
        }
        @Override
        public void onDragEnd(int x, int y) {
            int dx = x-startX, dy = y-startY;
            int ax = Math.abs(dx), ay = Math.abs(dy);
            Dir dir = null;
            if (ay > 2*ax) dir = dy>0 ? Dir.DOWN : Dir.UP;
            else if ( ax > 2*ay) dir = dx>0 ? Dir.RIGHT : Dir.LEFT;
            if (dir!=null)
                model.setSnakeDirection(dir);
            startX = -1;
        }
        @Override
        public void onDragCancel() { startX=-1; }

    }

    public void onBeat(long beat, long time) {
        if (Level.snakeIsDead() || model.levelFinished())
            panel.removeHeartbeatListener();
        else step();
    }

    private void step() {
        model.step();
        if (Level.snakeIsDead()) gameOver();
        else if (model.levelFinished()) {
            if (levelNum == 3) gameOver();
            else {
                buttonNextLvl = true;
                nextLevel.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void gameOver(){
        buttonFinish = true;
        nextLevel.setVisibility(View.INVISIBLE);
        gameOver.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);
    }

    public Context getContext() { return this; }

}
