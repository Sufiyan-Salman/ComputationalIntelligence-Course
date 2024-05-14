import pandas as pd

# Load the dataset
file_path = '/home/sufiyan/Desktop/Idea Projects/Learning/src/ann dataset.xlsx'
dataset = pd.read_excel(file_path)

# Perform Min-Max Scaling
min_vals = dataset.min()
max_vals = dataset.max()
normalized_dataset = (dataset - min_vals) / (max_vals - min_vals)
print(# Display the first few rows of the normalized dataset
normalized_dataset.head())
# Save the normalized dataset to a file with space-separated values
output_file_path = '/home/sufiyan/Desktop/Idea Projects/Learning/src/normalized_data.txt'
with open(output_file_path, 'w') as f:
    for row in normalized_dataset.itertuples(index=False, name=None):
        f.write(' '.join(map(str, row)) + '\n')
