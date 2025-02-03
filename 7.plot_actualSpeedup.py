import pandas as pd
import matplotlib.pyplot as plt

# Read the CSV file with speedup results
df = pd.read_csv('./results/05.results.csv')

# Create a figure and 2x2 grid of subplots
fig, axes = plt.subplots(2, 2, figsize=(12, 10))
axes = axes.flatten()  # Flatten the axes array for easier iteration


grid_configs = [
    (500, 600),
    (500, 1000),
    (1000, 600),
    (1000, 1000),
]

for i in range(len(grid_configs)):
    config = grid_configs[i]
    df_n = df.loc[df['gridSize'].eq(config[0]) & df['steps'].eq(config[1])]
    
    # Select the subplot for the current grid configuration
    ax = axes[i]
    
    ax.plot(df_n['NUM_THREADS'], df_n['Actual Speedup'], marker='o', label='Actual Speedup')
    
    # Add labels, title, and legend
    ax.set_title(f'GridSize={config[0]}, Steps={config[1]}', fontsize=12)
    ax.set_xlabel('Number of Threads', fontsize=10)
    ax.set_ylabel('Speedup', fontsize=10)
    ax.legend(fontsize=9)
    ax.grid(True)

# Adjust layout to avoid overlap
plt.tight_layout()

# Save the plot
plt.savefig('./Images/speedup_vs_threads.png', dpi=300)

# Show the plot
# plt.show()
print("Plots saved as PNG files in the './Images' folder.")