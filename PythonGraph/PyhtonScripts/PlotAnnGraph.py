import matplotlib.pyplot as plt

# Lists to store recordNo, outputs, and fitness values
recordNo = []
predicted_values = []
actual_values = []


def load_from_file(filename):
    with open(filename, 'r') as file:
        lines = file.readlines()
        i = 1
        for line in lines:
            # Split each line into a list of values
            values = [float(val) for val in line.strip().split()]
            recordNo.append(i)
            predicted_values.append(values[0])
            actual_values.append(values[1])
            i += 1
    print(recordNo)
    print(predicted_values)
    print(actual_values)

def plot_data():
    plt.scatter(recordNo, predicted_values, c=predicted_values, cmap='viridis', marker='o')
    plt.plot(recordNo, predicted_values, linestyle='-', color='red', label='Predicted Values')
    plt.scatter(recordNo, actual_values, c=actual_values, cmap='viridis', marker='o')
    plt.plot(recordNo, actual_values, linestyle='-', color='blue', label='Actual Values')

    plt.xlabel('RecordNo')
    plt.ylabel('Outputs')
    plt.title('Data Plot')
    plt.legend()
    plt.grid(True)
    plt.savefig('pngs/ann_graph.png')
    # plt.show()

filename = '/home/sufiyan/Desktop/Idea Projects/Learning/src/ann_output.txt'
load_from_file(filename)
plot_data()

