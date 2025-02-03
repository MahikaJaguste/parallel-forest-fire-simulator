import subprocess
import csv

# Configurations
grid_configs = [
    (500, 600),
    (500, 1000),
    (1000, 600),
    (1000, 1000),
]
iterations = 3
thread_counts = [2, 4, 8]

subprocess.run(["javac", "Driver.java"])

# Output file for aggregated results
output_file = "01.executionTimes.csv"

# Function to read the time
def read_time(fileName):
    with open(fileName + ".txt", "r") as file:
        time = round(float(file.read().strip()), 2)
    return time

def run_simulation(mode, grid_size, steps, growth_probability, burn_probability, thread_count=None):
    args = ["java", "Driver", mode, str(grid_size), str(steps), str(growth_probability), str(burn_probability), str(iterations)]
    if mode == "parallel":
        args.append(str(thread_count))
    result = subprocess.run(args, capture_output=True, text=True)
    print(result.stdout)

def main(): 
    with open("./results/01.results.csv", "w", newline='') as csvfile:
        csvwriter = csv.writer(csvfile)
        # Write the header
        csvwriter.writerow(['gridSize', 'steps', 'Serial Time', 'NUM_THREADS', 'Parallel Total Time'])

        serial_time = 0
        parallel_total_time = 0

        # Run simulations and gather results
        for grid_size, steps in grid_configs:
            # Run serial simulation
            run_simulation("serial", grid_size, steps, 0.15, 0.25)
            serial_time = read_time("serial")
           
            # Run parallel simulations
            for thread_count in thread_counts:
                run_simulation("parallel", grid_size, steps, 0.15, 0.25, thread_count)
                parallel_total_time = read_time("parallel")

                csvwriter.writerow([grid_size, steps, serial_time, thread_count, parallel_total_time])
  
    print("New CSV file '01.results.csv' has been created with execution times values.")

main()

