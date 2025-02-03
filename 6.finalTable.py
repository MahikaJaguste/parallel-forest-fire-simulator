import pandas as pd
from tabulate import tabulate
import textwrap

# Step 1: Load the two CSV files into dataframes
df = pd.read_csv('./results/05.results.csv')

# final_columns = ['gridSize','Serial Time','NUM_THREADS', 'Parallel Section Time','Parallel Total Time', 'Actual Speedup', 'p','Amdahl Speedup', "Max Speedup"]
# df = df[final_columns]

# List of columns to apply the masking
cols_to_mask = ['gridSize', 'steps', 'Serial Time', 'p', 'Max Speedup']

# Create a helper column to identify the first row for each (gridSize, steps) pair
df['is_first'] = ~df.duplicated(subset=['gridSize', 'steps'])

# Mask columns based on the helper column
for col in cols_to_mask:
    df[col] = df[col].where(df['is_first'], '')

# Drop the helper column
df = df.drop(columns=['is_first'])
cols = list(df.columns)
df['Case'] = ['1','','','2','','','3','','','4','', '']
df = df[['Case'] + cols]

# headers = ["gridSize", """Serial Time (ms)", "#threads", "Parallel Section Time (ms)", "Parallel Total Time (ms)", "Actual Speedup", "p", "Amdahl Speedup", "Max Speedup"]

headers = [
    "\n".join(textwrap.wrap("Case", width=4)),
    "\n".join(textwrap.wrap("Grid size", width=10)),
    "\n".join(textwrap.wrap("Steps", width=10)),
    "\n".join(textwrap.wrap("Serial Time (ms)", width=10)),
    "#threads", 
    "\n".join(textwrap.wrap("Parallel Time (ms)", width=10)), 
    "\n".join(textwrap.wrap("Actual Speedup", width=10)),
    "\n".join(textwrap.wrap("Efficiency", width=10)),
    "\n".join(textwrap.wrap("F_enhanced", width=10)),
    "\n".join(textwrap.wrap("Amdahl Speedup", width=10)),
    "\n".join(textwrap.wrap("Max theoretical Speedup", width=10)),
    "\n".join(textwrap.wrap("Gustafson-Barsis Speedup", width=10)),
    "\n".join(textwrap.wrap("Karp-Flatt Metric", width=10)),

]

# Display the table using tabulate with borders, formatted to 2 decimal places
table = tabulate(df, 
    headers,
    tablefmt='grid',
    numalign="right",
    stralign="right",
    showindex=False,
    floatfmt=".2f",  # Format numbers to 2 decimal places
)

# Print the formatted table
print(table)
