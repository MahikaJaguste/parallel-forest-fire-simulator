import pandas as pd

# Load the CSV file with Amdahl's Speedup
df = pd.read_csv('./results/03.results.csv')

# Step 1: Calculate Gustafson's Speedup for each row
# Formula: S(n) = p * n + (1 - p)
df['Gustafson Speedup'] = (df['p'] * df['NUM_THREADS'] + (1 - df['p'])).round(2)

# Step 2: Write the updated DataFrame to a new CSV file
df.to_csv('./results/04.results.csv', index=False)

print("Results with Gustafson's Speedup written to '04.results.csv'")
