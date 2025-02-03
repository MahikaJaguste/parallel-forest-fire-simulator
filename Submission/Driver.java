/*
 * Student Name: Mahika Jaguste
 * Student ID: 24016454
 */
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
    private static final double NANO_TO_MILLI = 1_000_000.0;

    public static void main(String[] args) throws InterruptedException, IOException {

        // Initialise the grids and store in a CSV to use the same forest for all performance tests
        // Forest.initializeGrid(500, .70, 0.0005);
        // Forest.initializeGrid(1000, .70, 0.0005);
    
        if (args.length < 6) {
            System.out.println("Usage: java Driver <mode> <gridSize> <steps> <growthProbability> <burnProbability> <iterations> [<threadCount>]");
            return;
        }

        // serial or parallel execution
        String mode = args[0];
        // parse the grid size, simulation steps, growth probability and burn probability
        int gridSize = Integer.parseInt(args[1]);
        int steps = Integer.parseInt(args[2]);
        double growthProbability = Double.parseDouble(args[3]);
        double burnProbability = Double.parseDouble(args[4]);
        // iterations to get average execution time
        int iterations = Integer.parseInt(args[5]);
        // parse thread count for parallel execution
        int threadCount = mode.equals("parallel") ? Integer.parseInt(args[6]) : 1;

        System.out.println(mode + " execution for grid size = " + gridSize + " and steps = " + steps + ".");
        if(mode.equals("parallel")) {
            System.out.println("Threads = " + threadCount);
        }

        double serialTotalTime = 0.0;
        double parallelTotalTime = 0.0;

        for (int iter = 0; iter < iterations; iter++) {
            System.out.println("Iteration " + iter);
            // load the forest from the CSV and store it in grid
            Forest forest = new Forest(gridSize, growthProbability, burnProbability);
            int[] counts = {0,0,0};
            
            if (mode.equals("serial")) {
                // serial simulation
                SerialForestFireSimulation serialSimulation = new SerialForestFireSimulation(forest, steps);
                long serialStartTime = System.nanoTime();
                serialSimulation.simulate();
                long serialEndTime = System.nanoTime();
                serialTotalTime += (serialEndTime - serialStartTime) / NANO_TO_MILLI;
                counts = serialSimulation.stepWiseCounts[steps-1];
            } else if (mode.equals("parallel")) {
                // parallel simulation
                ParallelForestFireSimulation parallelSimulation = new ParallelForestFireSimulation(forest, steps, threadCount);
                long parallelStartTime = System.nanoTime();
                parallelSimulation.simulate();
                long parallelEndTime = System.nanoTime();
                parallelTotalTime += (parallelEndTime - parallelStartTime) / NANO_TO_MILLI;
                counts = parallelSimulation.stepWiseCounts[steps-1];
            }

            // printing the counts stored at the last step
            System.out.println("Counts at last step: ");
            System.out.println("Step " + (steps) + 
                                    ": Empty = " + counts[0] + 
                                    ", Tree = " + counts[1] + 
                                    ", Burning = " + counts[2] + 
                                    ", Total = " + (counts[0] + counts[1] + counts[2])
                                );
        }

        // calculate average execution time
        double avgTime = 0.0;
        if(mode.equals("serial")) {
            avgTime = serialTotalTime / iterations;
        } else if(mode.equals("parallel")) {
            avgTime = parallelTotalTime / iterations; 
        }

        writeTimeToFile(mode, avgTime);

    }

    // write results to file for python script to read and analyse
    public static void writeTimeToFile(String mode, double time) {
        try (FileWriter writer = new FileWriter(mode + ".txt")) {
            writer.append("" + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
