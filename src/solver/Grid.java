package solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class Grid {
    
    private static int DIMENSION = 9;
    private static int NCELLS = DIMENSION*DIMENSION;

    private Cell[][] grid;

    public Grid(String filename) {
        grid = new Cell[DIMENSION][DIMENSION];

        File file = new File(filename);
        Charset encoding = Charset.defaultCharset();
        try {
            InputStream in = new FileInputStream(file); 
            Reader reader = new InputStreamReader(in, encoding);
            Reader buffer = new BufferedReader(reader);
            int r;
            int i = 0;
            while ((r = buffer.read()) != -1) {
                char c = (char) r;

                if (c == '\n') continue;

                if (c == '*') {
                    grid[i/DIMENSION][i%DIMENSION] = new Cell(true, 0);
                } else {
                    grid[i/DIMENSION][i%DIMENSION] = new Cell(false, Character.getNumericValue(c));
                }
                i++;
            }
        } catch (Exception e) {e.printStackTrace();};
    }

    public String toStringEmptyGrid() {
        String s = "";
        for (int i = 0; i < DIMENSION; i++) {
            if (i%3==0) {
                for (int j = 0; j < 25; j++)
                    s += "-";
                s += "\n";
            }
            for (int j = 0; j < DIMENSION; j++) {
                if (j==0) s += "| ";
                s += ((grid[i][j].getIsModifiable())? "*": grid[i][j].getValue()) + " ";
                if (j%3 == 2) s += "| ";
            }
            s += "\n";
        }
        for (int j = 0; j < 25; j++)
            s += "-";
        s += "\n";
        return s;
    }

    public String toStringGrid() {
        String s = "";
        for (int i = 0; i < DIMENSION; i++) {
            if (i%3==0) {
                for (int j = 0; j < 25; j++)
                    s += "-";
                s += "\n";
            }
            for (int j = 0; j < DIMENSION; j++) {
                if (j==0) s += "| ";
                s += grid[i][j].getValue() + " ";
                if (j%3 == 2) s += "| ";
            }
            s += "\n";
        }
        for (int j = 0; j < 25; j++)
            s += "-";
        s += "\n";
        return s;
    }

    public int findNextValue(int i, int j) {

        outerLoop: for (int next = grid[i][j].getValue() + 1; next <= DIMENSION; next++) {
            // Check on line and column if next is already present
            for (int k = 0; k < DIMENSION; k++) {
                if (grid[i][k].getValue() == next || grid[k][j].getValue() == next) { // Check on line and column
                    continue outerLoop;
                } 
            }

            // Check on block if value if next is already present
            for (int a = 3 * (i/3); a < 3*(i/3)+3; a++) {
                for (int b = 3 * (j/3); b < 3*(j/3)+3; b++) {
                    if (grid[a][b].getValue() == next) {
                        continue outerLoop;
                    }
                }
            }
            return next;
        }
    return 0;
    }

    public void fillGrid() throws Exception {
        int count = 0;

        for (int a = 0; a < NCELLS; a++) {
            if (grid[a/DIMENSION][a%DIMENSION].getIsModifiable()) {
                int next = findNextValue(a/DIMENSION, a%DIMENSION); // Return 0 if no superior value is possible
                grid[a/DIMENSION][a%DIMENSION].setValue(next);
                count++;

                while (next == 0 && a >= 0) { // Backtracking
                    a--;
                    if (a >= 0 && grid[a/DIMENSION][a%DIMENSION].getIsModifiable()) {
                        next = findNextValue(a/DIMENSION, a%DIMENSION);
                        grid[a/DIMENSION][a%DIMENSION].setValue(next);
                        count++;
                    }
                }
            }
        }
        System.out.println("Stats: "+count+" modifications");
    }

    public static void main(String[] args) throws Exception {

        Grid grid = new Grid(System.getProperty("user.dir")+"/src/input/difficult_9x9_2.txt");
        //        Grid grid = new Grid(System.getProperty("user.dir")+"/src/input/easy_9x9_0.txt");
        System.out.print(grid.toStringEmptyGrid());

        System.out.print("Attempting to fill grid");
        grid.fillGrid();
        System.out.println(" -> Done");

        System.out.println(grid.toStringGrid());


    }

}
