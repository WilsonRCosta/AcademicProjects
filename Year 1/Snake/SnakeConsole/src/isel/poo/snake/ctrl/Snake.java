package isel.poo.snake.ctrl;

import isel.poo.console.Window;
import isel.poo.console.tile.Tile;
import isel.poo.console.tile.TilePanel;
import isel.leic.pg.Console;
import isel.poo.snake.model.*;
import isel.poo.snake.view.CellTile;
import isel.poo.snake.view.EmptyTile;
import isel.poo.snake.view.StatusPanel;

import static java.awt.event.KeyEvent.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Main class in console mode for the "Snake" game.
 * Performs interaction with the user.<br/>
 * Implements the Controller in the MVC model,
 * making the connection between the model and the viewer specific to the console mode.
 */
public class Snake {
    public static void main(String[] args) {
        Snake ctrl = new Snake();
        ctrl.run();
    }

    private static final int WIN_HEIGHT = 24, WIN_WIDTH = 28;
    private Window win = new Window("Snake",WIN_HEIGHT, WIN_WIDTH +StatusPanel.WIDTH); // The console window
    private StatusPanel status = new StatusPanel(WIN_WIDTH); // View of Level, Apples and Score

    private static final String LEVELS_FILE = "levels.txt"; // Name of levels file
    private Game model;                                     // Model of game
    private Level level;                                    // Model of current level
    private TilePanel view;                                 // View of cells
    private boolean escaped = false;
    private boolean paused = false;

    private static final int STEP_TIME = 200;               // Milliseconds by step
    private long time;                                      // Current time for next step

    /**
     * Main game loop.
     * Returns when there are no more levels or the player gives up.
     */
    private void run() {
        try (InputStream file = new FileInputStream(LEVELS_FILE)) { // Open description file
            model = new Game(file);                                 // Create game model
            model.setListener(updater);                             // Set listener of game
            while ((level = model.loadNextLevel() ) != null )       // Load level model
                if (!playLevel() || !win.question("Next level")) {  // Play level
                    win.message("Bye.");
                    return;
                }
            win.message("No more Levels");
        } catch (Loader.LevelFormatException e) {
            win.message(e.getMessage());
            System.out.println(e+", Line "+e.getLineNumber()+": "+e.getLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally { win.close(); }                                    // Close console window
    }


    /**
     * Main loop of each level.
     * @return true - the level has been completed. false - the player has given up.
     */
    private boolean playLevel() {
        // Opens panel of tiles with dimensions appropriate to the current level.
        // Starts the viewer for each model cell.
        // Shows the initial state of all cells in the model.
        int height = level.getHeight(), width = level.getWidth();
        view = new TilePanel(height,width,CellTile.SIDE);               // Create view for cells
        win.clear();                                                    // Clear area of previous level
        view.center(WIN_HEIGHT,WIN_WIDTH);                              // Center view in area
        status.setLevel(level.getNumber());                             // Update status View
        status.setApples(level.getRemainingApples());
        status.setScore(model.getScore());


        for (int l = 0; l < height; l++)                                // Create each tile for each cell
            for (int c = 0; c < width; c++){
                view.setTile(l,c, CellTile.tileOf( level.getCell(l,c) ));
            }
        level.setObserver(updater);                                     // Set listener of level
        time = System.currentTimeMillis();                              // Set step time
        do
            play();                                                      // Process keys and make a step
        while ( !escaped && !level.isFinished() );

        if (escaped || level.snakeIsDead()) return false;
        win.message("You win");
        return true;                   // Verify win conditions; false: finished without complete
    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
        // Game.Listener
        @Override
        public void scoreUpdated(int score) { status.setScore( score ); }
        // Level.Listener
        @Override
        public void cellUpdated(int l, int c, Cell cell) { view.getTile(l,c).repaint(); }
        @Override
        public void cellCreated(int l, int c, Cell cell) { view.setTile(l,c,CellTile.tileOf(cell)); }
        @Override
        public void cellRemoved(int l, int c) { view.setTile(l,c,new EmptyTile()); }
        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
            Tile tile = view.getTile(fromL,fromC);
            assert !(tile instanceof EmptyTile);
            view.setTile(toL,toC,tile);
            cellRemoved(fromL,fromC);
        }
        @Override
        public void applesUpdated(int apples) { status.setApples(apples); }
    }
    private Updater updater = new Updater();

    /**
     * Process key pressed and makes one step.
     */
    private void play() {
        time += STEP_TIME;                  // Adjust step time
        int key = getKeyPressed();          // Wait a step time and read a key
        Dir dir;
        if (key > 0) {
             dir = null;
            switch (key) {
                case VK_UP: dir = Dir.UP; break;
                case VK_DOWN: dir = Dir.DOWN; break;
                case VK_LEFT: dir = Dir.LEFT; break;
                case VK_RIGHT: dir = Dir.RIGHT; break;
                case VK_ESCAPE: escaped=true; return;
                case VK_PAUSE:
                    paused = !paused;
                    win.state(paused ? "PAUSED" : null);
                    return;
            }
            if (dir!=null) level.setSnakeDirection(dir);
        }
        if (!paused) level.step();
    }

    private int lastKey = Console.NO_KEY;  // Only used by getKeyPressed to store lastKey
    /**
     * Reads a key during the time of a step
     * @return the key code or Console.NOKEY (negative value)
     */
    private int getKeyPressed() {
        long waitTime;          // Remaining time until the next step
        int key;                // Code of current key pressed
        do {
            waitTime = time - System.currentTimeMillis();
            if (waitTime<=0)
                return Console.NO_KEY;
            key = Console.waitKeyPressed(waitTime);
        } while (key == lastKey);           // Is the same key pressed
        lastKey = key;                      // Store the current key
        waitTime = time - System.currentTimeMillis();
        if (waitTime>0) Console.sleep(waitTime); // Waits remaining time after read a key
        return key;
    }
}
