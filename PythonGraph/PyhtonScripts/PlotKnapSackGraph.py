import matplotlib
import pickle
import matplotlib.pyplot as plt
import json

# Lists to store x, y, and fitness values
generation = []
weight_values = []
worth_values = []


def load_from_file(filename):
    with open(filename, 'r', newline='') as file:
        lines = file.readlines()
        i = 1
        for line in lines:
            # Split each line into a list of values
            values = [float(val) for val in line.strip().split()]
            generation.append(i)
            # weight_values.append(values[1])
            weight_values.append(values[0]) #weight
            worth_values.append(values[1]) #worth
            i += 1
            print(values)
    print(generation)
    # with open(filename, 'rb') as file:
    #     data = pickle.load(file)
    # return data
    # Print the loaded data
    # print(data)
    # return data


def plot_fitness_data(data):

    plt.scatter(weight_values, worth_values, c=worth_values, cmap='viridis', marker='o')
    plt.plot(weight_values, worth_values, linestyle='-', color='black', label='Connecting Lines')
    # plt.scatter(recordNo, actual_values, c=actual_values, cmap='viridis', marker='o')
    # plt.plot(recordNo, actual_values, linestyle='-', color='blue', label='Actual Values')

    plt.xlabel('Weight')
    plt.ylabel('Worth')
    plt.title('Fitness Data Plot')
    plt.colorbar(label='Fitness')
    plt.xticks(weight_values)
    # plt.show()
    plt.savefig('pngs/knapsack_plot.png')  # Save the plot to a file


if __name__ == "__main__":
    filename = "/home/sufiyan/Desktop/Idea Projects/Learning/src/knapsack_data.txt"
    fitness_data = load_from_file(filename)
    plot_fitness_data(fitness_data)
