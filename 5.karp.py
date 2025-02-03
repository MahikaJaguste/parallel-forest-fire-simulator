import pandas as pd

# Load the CSV file with Gustafson's Speedup
df = pd.read_csv('./results/04.results.csv')

# Step 1: Calculate the Karp-Flatt Metric for each row
# Formula: e(n) = (1/S(n) - 1/n) / (1 - 1/n)
df['Karp-Flatt Metric'] = (
    ((1 / df['Actual Speedup']) - (1 / df['NUM_THREADS'])) / (1 - (1 / df['NUM_THREADS']))
).round(4)

# Step 2: Write the updated DataFrame to a new CSV file
df.to_csv('./results/05.results.csv', index=False)

print("Results with Karp-Flatt Metric written to '05.results.csv'")
