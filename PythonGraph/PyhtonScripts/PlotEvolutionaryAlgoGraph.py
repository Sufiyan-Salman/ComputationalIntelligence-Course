import matplotlib
import pickle
import matplotlib.pyplot as plt
import json

# Lists to store x, y, and fitness values
generation = []
y_values = []
fitness_values = []


def load_from_file(filename):
    with open(filename, 'r', newline='') as file:
        lines = file.readlines()
        i = 1
        for line in lines:
            # Split each line into a list of values
            values = [float(val) for val in line.strip().split()]
            generation.append(i)
            # y_values.append(values[1])
            fitness_values.append(values[2]) #for knapsack
            # fitness_values.append(values[2]) #for knapsack
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

    plt.scatter(generation, fitness_values, c=fitness_values, cmap='viridis', marker='o')
    plt.plot(generation, fitness_values, linestyle='-', color='black', label='Connecting Lines')

    plt.xlabel('Generation')
    plt.ylabel('Fitness')
    plt.title('Fitness Data Plot')
    plt.colorbar(label='Fitness')
    plt.xticks(generation)
    # plt.show()
    plt.savefig('pngs/fitness_plot.png')  # Save the plot to a file


if __name__ == "__main__":
    filename = "/home/sufiyan/Desktop/Idea Projects/Learning/src/fitness_data.txt"
    fitness_data = load_from_file(filename)
    plot_fitness_data(fitness_data)
