import subprocess

# List of Python script file names
scripts = [
    # '1.executionTimes.py', 

    '2.actualSpeedup.py', 
    '3.p.py',
    '4.gustafson.py',
    '5.karp.py',
    '6.finalTable.py',

    # '7.plot_actualSpeedup.py',
    '8.plot_efficiency.py',
    '9.plot_speedups.py',
    '10.plot_karp.py',
    
]  # Add more file names as needed

# Loop through each script and execute them
for script in scripts:
    print(f"Running {script}...")
    
    # Run the script using subprocess and capture the output
    try:
        result = subprocess.run(['python', script], capture_output=True, text=True, check=True)
        
        # Print the output of the script
        print(f"Output of {script}:\n{result.stdout}")
        
    except subprocess.CalledProcessError as e:
        # If the script fails, print the error message
        print(f"Error occurred while running {script}:\n{e.stderr}")
    
    print("=" * 40)  # Separator for better readability
