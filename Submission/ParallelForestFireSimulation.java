/*
 * Student Name: Mahika Jaguste
 * Student ID: 24016454
 */
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;


public class ParallelForestFireSimulation {
    public final Forest forest;
    private final int steps;
    private final int threadCount;

    // atomic integers to hold cell type counts for each step - global across all threads
    public final AtomicInteger treeCount = new AtomicInteger(0);
    public final AtomicInteger burningCount = new AtomicInteger(0);
    public final AtomicInteger emptyCount = new AtomicInteger(0);

    // array and index to store cell type counts at each step
    public final int[][] stepWiseCounts;
    public int currentStep = 0;

    public ParallelForestFireSimulation(Forest forest, int steps, int threadCount) {
        this.forest = forest;
        this.steps = steps;
        this.threadCount = threadCount;
        this.stepWiseCounts = new int[steps][3];
    }

    public void simulate() throws InterruptedException {
        
        // create a thread pool with threadCount threads
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // at each step, stores the cell type counts in stepWiseCounts and resets counts to 0 for next step
        Runnable countCheckpointTask = () -> {
            countCheckpoint();
        };

        // barrier which all threads must reach after computing next state
        // it takes countCheckpointTask as a task to be done by the last arriving thread
        CyclicBarrier barrier1 = new CyclicBarrier(threadCount, countCheckpointTask);

        // barrier which all threads must reach after they update the original grid with the next state
        CyclicBarrier barrier2 = new CyclicBarrier(threadCount);

        // splitting rows among threads
        int baseRowsPerThread = forest.gridSize / threadCount;
        // handling the case where threadCount does not exactly divide the gridSize
        int extraRows = forest.gridSize % threadCount;

        for (int threadId = 0; threadId < threadCount; threadId++) {
            // allocate a section of rows to a worker
            int startRow = threadId * baseRowsPerThread + Math.min(threadId, extraRows);
            int endRow = startRow + baseRowsPerThread + (threadId < extraRows ? 1 : 0);
            
            Runnable workerTask = new Worker(startRow, endRow, steps, this, barrier1, barrier2);
            // submit the worker task to the executor service to handle
            executor.execute(workerTask);
        }

        // stops the executor service from listening to any further tasks
        // it will wait till all existing tasks are completed or it timeout occurs
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }


    private void countCheckpoint() {
        // stores the cell type counts for each step
        int[] globalCounts = {
            emptyCount.get(),
            treeCount.get(),
            burningCount.get()
        };

        stepWiseCounts[currentStep++] = globalCounts;

        // and resets them to 0
        emptyCount.set(0);
        treeCount.set(0);
        burningCount.set(0);
    }

}