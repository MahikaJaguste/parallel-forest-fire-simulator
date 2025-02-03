/*
 * Student Name: Mahika Jaguste
 * Student ID: 24016454
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Forest {
    // cell states
    public static final int EMPTY = 0;
    public static final int TREE = 1;
    public static final int BURNING = 2;

    public final int gridSize;
    public final double growthProbability;
    public final double burnProbability;

    public int[][] grid;
    public int[][] nextGrid;

    Random random = new Random();

    Forest(int gridSize, double growthProbability, double burnProbability) {
        this.gridSize = gridSize;
        this.growthProbability = growthProbability;
        this.burnProbability = burnProbability;
        this.grid = new int[gridSize][gridSize];
        this.nextGrid = new int[gridSize][gridSize];
        loadGridFromCSV(gridSize);
    }

    // takes in percentage of trees and percentage of burning trees
    // initialises cell states accordingly
    // saves forest grid to CSV
    public static void initializeGrid(int gridSize, double treePercentage, double burningPercentage) {
        Random random = new Random();
        int totalCells = gridSize * gridSize;
        int treeCount = (int)(totalCells * treePercentage);
        int burningCount = (int)(treeCount * burningPercentage);
        int[][] grid = new int[gridSize][gridSize];

        // Initialize all cells to EMPTY
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = EMPTY;
            }
        }

        // Randomly assign TREE cells
        while (treeCount > 0) {
            int i = random.nextInt(gridSize);
            int j = random.nextInt(gridSize);
            if (grid[i][j] == EMPTY) {
                grid[i][j] = TREE;
                treeCount--;
            }
        }

        // Randomly assign BURNING cells from TREE cells
        while (burningCount > 0) {
            int i = random.nextInt(gridSize);
            int j = random.nextInt(gridSize);
            if (grid[i][j] == TREE) {
                grid[i][j] = BURNING;
                burningCount--;
            }
        }

        // Save the grid to a CSV file
        saveGridToCSV(gridSize, grid);
    }

    private static void saveGridToCSV(int gridSize, int[][] grid) {
        String fileName = "./forest_" + gridSize + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    writer.write(grid[i][j] + (j < gridSize - 1 ? "," : ""));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // re-using the stored forest grid
    private void loadGridFromCSV(int gridSize) {
        String fileName = "forest_" + gridSize + ".csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null && i < gridSize) {
                String[] values = line.split(",");
                for (int j = 0; j < gridSize; j++) {
                    grid[i][j] = Integer.parseInt(values[j]);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // helper to get next state of cell at position (x, y)
    public int getCellNextState(int x, int y) {
        int currentState = grid[x][y];
        if (currentState == EMPTY) {
            // empty cell grows a tree in the next step with growthProbability
            return random.nextDouble() < growthProbability ? TREE : EMPTY;
        } else if (currentState == TREE) {
            // healthy tree burns in the next step if
            // a. any of its neighbours is burning
            // b. with burnProbability
            boolean neighborBurning = isNeighborBurning(x, y);
            return neighborBurning && random.nextDouble() < burnProbability ? BURNING : TREE;
        } else { // BURNING
            // burning tree becomes empty in next step
            return EMPTY;
        }
    }

    // checks states of all 8 neighbours of the cell at position (x, y)
    private boolean isNeighborBurning(int x, int y) {
        int[] dx = {-1, 0, 1, 0, -1, 1, -1, 1};
        int[] dy = {0, -1, 0, 1, -1, -1, 1, 1};

        for (int d = 0; d < 8; d++) {
            // wrapping around boundaries and corners
            int nx = (x + dx[d] + gridSize) % gridSize;
            int ny = (y + dy[d] + gridSize) % gridSize;

            if (grid[nx][ny] == BURNING) {
                return true;
            }
        }
        return false;
    }

}
    
