package isel.poo.snake.model;


import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Loads a game level from the file read with the scanner indicated in the constructor.<br/>
 * The file contains several levels.<br/>
 * Each level has a number from 1 to N.<br/><br/>
 * The first line of description for a level must conform to the format:<br/>
 * <code>#NNN HEIGHT x WIDTH</code><br/>
 * Where: <code>NNN</code> is the level number.<br/>
 * <code>HEIGHT</code> is the number of lines.<br/>
 * <code>WIDTH</code> is the number of columns.<br/>
 *
 */
public class Loader {
    private final Scanner in;   // Scanner used to read the file
    private int lineNumber;     // Current line number
    private String line;        // Text of current line

    private Level model;      // The loaded model
    private int height, width;   // Dimensions of current level

    /**
     * Build the loader to read it from the file through the scanner
     * @param in The scanner to use
     */
    Loader(Scanner in) {
        this.in = in;
    }

    /**
     * Reads the level identified by the number.<br/>
     * This is the only public method of the class.<br/>
     * @param levelNumber The level number
     * @return The model for the loaded level or null if level not found
     * @throws LevelFormatException If an error is found in the file
     */
    Level load(int levelNumber) throws LevelFormatException {
        if (!findHeader(levelNumber))   // Find the header line              <-----------------------------------------------      MUDAR O NIVEL AQUI
            return null;
        model = new Level(levelNumber,height,width);    // Build the model
        loadGrid();                         // Load cells information
        return model;
    }

    /**
     * Read the square grid and instantiate each square according to its description.<br/>
     * @throws LevelFormatException If an error is found in square descriptions
     */
    private void loadGrid() throws LevelFormatException {
        for(int l=0; l<height ; ++lineNumber,++l) {
            line = in.nextLine();                  // Read a line of cells
            if (line.length()>width)               // Verify number of cells in line
                error("Wrong number of cells in line");
            for(int c = 0; c<line.length() ; c++) {
                char type = line.charAt(c);
                if (type!=' ') {
                    Cell cell = createCell(type);
                    model.putCell(l, c, cell);           // Add cell to the model
                }
            }
        }
    }

    private Cell createCell(char type) throws LevelFormatException {
        Cell cell = Cell.newInstance(type);     // Create a cell identified by first char
        if (cell==null)
            error("Unknown cell type ("+type+")");
        return cell;
    }

    /**
     * Find the header line for the level<br/>
     * Stores the dimensions of the level in <code>height</code> and <code>width</code> fields.
     * @param level The level number
     * @throws LevelFormatException If an errors is found in the file or level not found.
     */
    private boolean findHeader(int level) throws LevelFormatException {
        try {
            int idx;
            for (lineNumber = 1; ; ++lineNumber) {
                line = in.nextLine();
                if (line.length() == 0 || line.charAt(0) != '#') continue;
                if ((idx = line.indexOf(' ')) <= 1) error("Invalid header line");
                if (Integer.parseInt(line.substring(1, idx)) == level) break;
            }
            int idxSep = line.indexOf('x',idx+1);
            if (idxSep<=0) error("Missing dimensions of level "+level);
            height = Integer.parseInt(line.substring(idx+1,idxSep).trim());
            width = Integer.parseInt(line.substring(idxSep+1).trim());
        } catch (NumberFormatException e) {
            error("Invalid number");
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Helper method to launch a LevelFormatException in internal methods.
     * @param msg The exception message
     * @throws LevelFormatException
     */
    private void error(String msg) throws LevelFormatException {
        throw new LevelFormatException(msg);
    }

    /**
     * Launched when a level loading error is detected.
     * The message describes the type of error.
     * Has the line number and the line where the error was detected.
     */
    public class LevelFormatException extends Exception {
        LevelFormatException(String msg) {
            super(msg);
        }
        public int getLineNumber() { return lineNumber; }
        public String getLine() { return line; }
    }
}
