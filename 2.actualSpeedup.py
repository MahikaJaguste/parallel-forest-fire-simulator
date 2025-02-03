import pandas as pd

# Load the CSV file into a DataFrame
df = pd.read_csv('./results/01.results.csv')

# Step 1: Calculate the 'Actual Speedup' for each row
df['Actual Speedup'] = df['Serial Time'] / df['Parallel Total Time']
df['Actual Speedup'] = df['Actual Speedup'].round(2)

df['Efficiency'] = df['Serial Time'] / df['Parallel Total Time'] / df['NUM_THREADS']
df['Efficiency'] = df['Efficiency'].round(2)

# Step 3: Save the updated DataFrame into a new CSV file 'results2.csv'
df.to_csv('./results/02.results.csv', index=False)

print("New CSV file '02.results.csv' has been created with the Actual Speedup values.")
