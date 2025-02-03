# import pandas as pd

# # Load the CSV file into a DataFrame
# df = pd.read_csv('results2.csv')

# # Step 1: Calculate 'p' (fraction of code that is parallelized) 
# # calculate the average of 'Parallel Section Time' / 'Parallel Total Time' 
# p_value = round((df['Parallel Section Time'] / df['Parallel Total Time']).mean(), 4)

# maxSpeedup = 1 / (1-p_value)

# # Writing to file
# with open("pMaxSpeedup.txt", "w") as file:
#     # Writing data to a file
#     file.write(str(p_value) + "\n" + str(maxSpeedup))

# print("p value written to file pMaxSpeedup.txt")

import pandas as pd

# Load the CSV file into a DataFrame
df = pd.read_csv('./results/02.results.csv')

# Step 1: Calculate 'p' (fraction of code that is parallelized) for each row
# df['p'] = 0.9 # df['Parallel Section Time'] / df['Parallel Total Time']

# Step 2: Group by 'n' and calculate the mean of 'p' for each unique 'n'
# Assign this average 'p' value back to the rows for each 'n'
# df['p'] = df.groupby(['gridSize', 'steps'])['p'].transform('mean').round(4)
df['p'] = 0.0
df.loc[df['gridSize'] == 500, 'p'] = .95
df.loc[df['gridSize'] == 1000, 'p'] = .975

# # Step 3: Calculate Amdahl's speedup for each row
df['Amdahl Speedup'] = (1 / ((1 - df['p']) + df['p'] / df['NUM_THREADS'])).round(2)

df['Max Speedup'] = (1 / (1 - df['p'])).round(2)

# Step 4: Write the updated DataFrame to a new CSV file
# Keeping the original columns and appending 'p' and 'Amdahl Speedup'
df.to_csv('./results/03.results.csv', index=False)

print("Results with p and Amdahl speedup written to '03.results.csv'")
