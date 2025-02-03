/*
 * Student Name: Mahika Jaguste
 * Student ID: 24016454
 */
public class SerialForestFireSimulation {
    public final Forest forest;
    private final int steps;

    // array and index to store cell type counts at each step
    public final int[][] stepWiseCounts;
    public int currentStep = 0;

    public SerialForestFireSimulation(Forest forest, int steps) {
        this.forest = forest;
        this.steps = steps;
        this.stepWiseCounts = new int[steps][3];
    }

    public void simulate() {
        for (int step = 0; step < steps; step++) {
            int localTreeCount = 0;
            int localBurningCount = 0;
            int localEmptyCount = 0;

            // update cells in the entire grid and store the next state in next grid 
            // maintain cell type counts
            for (int i = 0; i < forest.gridSize; i++) {
                for (int j = 0; j < forest.gridSize; j++) {
                    int nextState = forest.getCellNextState(i, j);
                    forest.nextGrid[i][j] = nextState;
                    
                    if(nextState == Forest.EMPTY) {
                        localEmptyCount++;
                    } else if(nextState == Forest.TREE) {
                        localTreeCount++;
                    } else if(nextState == Forest.BURNING) {
                        localBurningCount++;
                    }
                }
            }

            // checkpoint the counts
            int[] counts = {
                localEmptyCount,
                localTreeCount,
                localBurningCount
            };
            stepWiseCounts[step] = counts;

            // update original grid with next state
            for (int i = 0; i < forest.gridSize; i++) {
                for (int j = 0; j < forest.gridSize; j++) {
                    forest.grid[i][j] = forest.nextGrid[i][j];
                }
            }
        }
    }
}
