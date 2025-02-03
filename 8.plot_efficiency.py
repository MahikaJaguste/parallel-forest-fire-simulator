import pandas as pd
import matplotlib.pyplot as plt

# Read the CSV file with speedup results
df = pd.read_csv('./results/05.results.csv')

# Set up the plot
plt.figure(figsize=(10, 6))


grid_configs = [
    (500, 600),
    (500, 1000),
    (1000, 600),
    (1000, 1000),
]

for i in range(len(grid_configs)):
    config = grid_configs[i]
    # Filter the data for the current n
    df_n = df.loc[df['gridSize'].eq(config[0]) & df['steps'].eq(config[1])]
    plt.plot(df_n['NUM_THREADS'], df_n['Efficiency'], marker='o', label=f'Case {i+1}')


# Add labels and title
plt.title('Efficiency vs. Number of Threads', fontsize=14)
plt.xlabel('Number of Threads', fontsize=12)
plt.ylabel('Efficiency', fontsize=12)
# plt.xticks(group['NUM_THREADS'])  # Ensure all thread counts are displayed on the x-axis
plt.legend(title='Problem sizes', fontsize=10)
plt.grid(True)

# Save the plot
plt.savefig('./Images/efficiency_vs_threads.png', dpi=300)

# Show the plot
# plt.show()
print("Plots saved as PNG files in the './Images' folder.")