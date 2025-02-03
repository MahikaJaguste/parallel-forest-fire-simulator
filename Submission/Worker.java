/*
 * Student Name: Mahika Jaguste
 * Student ID: 24016454
 */
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {
    private final int startRow;
    private final int endRow;
    private final int steps;
    private final ParallelForestFireSimulation simulation;
    private final Forest forest;
    private final CyclicBarrier barrier1;
    private final CyclicBarrier barrier2;

    public Worker(int startRow, int endRow, int steps, ParallelForestFireSimulation simulation, CyclicBarrier barrier1, CyclicBarrier barrier2) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.steps = steps;
        this.simulation = simulation;
        this.forest = simulation.forest;
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
    }

    // atomic updates to global cell type counts for each simulation step
    private void updateGlobalCounts(int localEmptyCount, int localTreeCount, int localBurningCount) {
        simulation.emptyCount.addAndGet(localEmptyCount);
        simulation.treeCount.addAndGet(localTreeCount);
        simulation.burningCount.addAndGet(localBurningCount);
    }

    @Override
    public void run() {
        // simulate steps
        for (int step = 0; step < steps; step++) {
            // maintain local cell type counts for each simulation step
            int localTreeCount = 0;
            int localBurningCount = 0;
            int localEmptyCount = 0;

            // update cells in the assigned section and store the next state in next grid 
            // maintain cell type counts for that section
            for (int i = startRow; i < endRow; i++) {
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

            // add these local counts to the global counts
            updateGlobalCounts(localEmptyCount, localTreeCount, localBurningCount);

            // wait for all threads to finish their updates
            try {
                barrier1.await();
                // the last thread to reach the barrier also checkpoints the global counts for this time step
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // update shared grid with next state
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < forest.gridSize; j++) {
                    forest.grid[i][j] = forest.nextGrid[i][j];
                }
            }

            // wait for all threads to finish their updates
            try {
                barrier2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}