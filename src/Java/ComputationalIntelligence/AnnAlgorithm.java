package Java.ComputationalIntelligence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AnnAlgorithm {

//    private static List<Double> weights = new ArrayList<>();
//    private static List<Double> biases = new ArrayList<>();
    private static List<Double> predictedOutputs = new ArrayList<>();
    private static List<Double> actualOutputs = new ArrayList<>();
    private static Map<String, Double> weightsAndBiases = new HashMap<>();
    private static List<Double> errors = new ArrayList<>();

    private static double O4;
    private static double O5;
    private static double O6;

    private static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
//        return 1 / (1 + Math.exp(-x));

    }

    private static double forwardFeed(double i1, double i2, double i3, double actualOutput) {
        double I4 = i1 * weightsAndBiases.get("w14") + i2 * weightsAndBiases.get("w24") + i3 * weightsAndBiases.get("w34") + weightsAndBiases.get("theta4");
        double I5 = i1 * weightsAndBiases.get("w15") + i2 * weightsAndBiases.get("w25") + i3 * weightsAndBiases.get("w35") + weightsAndBiases.get("theta5");
//        double I4 = i1 * weights.get(0) + i2 * weights.get(1) + i3 * weights.get(2) + biases.get(0);
//        double I5 = i1 * weights.get(3) + i2 * weights.get(4) + i3 * weights.get(5) + biases.get(1);
        O4 = sigmoid(I4);
        O5 = sigmoid(I5);
        double I6 = O4 * weightsAndBiases.get("w46") + O5 * weightsAndBiases.get("w56") + weightsAndBiases.get("theta6");
//        double I6 = O4 * weights.get(6) + O5 * weights.get(7) + biases.get(2);
        O6 = sigmoid(I6); // predicted output
        return calculateError(actualOutput);
    }

    private static double calculateError(double targetValue) {
        return O6 * (targetValue - O6) * (1 - O6);
    }

    private static double getChangeInWeight(double error, double output) {
        return error * output * LEARNING_FACTOR;
    }

    private static double getChangeInBias(double err) {
        return err * LEARNING_FACTOR;
    }

    private static void backPropagate(double errO6, double i1, double i2, double i3) {
        double errO4 = O4 * (1 - O4) * (errO6 * weightsAndBiases.get("w46")); // w46
//        double errO4 = O4 * (1 - O4) * errO6 * weights.get(6); // w46
        double errO5 = O5 * (1 - O5) * (errO6 * weightsAndBiases.get("w56")); // w56
//        double errO5 = O5 * (1 - O5) * errO6 * weights.get(7); // w56

        // Updating weights
        weightsAndBiases.replace("w14", weightsAndBiases.get("w14") + getChangeInWeight(errO4  ,i1));
        weightsAndBiases.replace("w24", weightsAndBiases.get("w24") + getChangeInWeight(errO4  ,i2));
        weightsAndBiases.replace("w34", weightsAndBiases.get("w34") + getChangeInWeight(errO4  ,i3));
        weightsAndBiases.replace("w15", weightsAndBiases.get("w15") + getChangeInWeight(errO5  ,i1));
        weightsAndBiases.replace("w25", weightsAndBiases.get("w25") + getChangeInWeight(errO5  ,i2));
        weightsAndBiases.replace("w35", weightsAndBiases.get("w35") + getChangeInWeight(errO5  ,i3));

        weightsAndBiases.replace("w46", weightsAndBiases.get("w46") + getChangeInWeight(errO6  ,O4));
        weightsAndBiases.replace("w56", weightsAndBiases.get("w56") + getChangeInWeight(errO6  ,O5));
//        weights.set(0, weights.get(0) + getChangeInWeight(i1, errO4));
//        weights.set(1, weights.get(1) + getChangeInWeight(i1, errO5));
//        weights.set(2, weights.get(2) + getChangeInWeight(i2, errO4));
//        weights.set(3, weights.get(3) + getChangeInWeight(i2, errO5));
//        weights.set(4, weights.get(4) + getChangeInWeight(i3, errO4));
//        weights.set(5, weights.get(5) + getChangeInWeight(i3, errO5));
//        weights.set(6, weights.get(6) + getChangeInWeight(O4, errO6));
//        weights.set(7, weights.get(7) + getChangeInWeight(O5, errO6));

        // Updating biases
        weightsAndBiases.replace("theta4", weightsAndBiases.get("theta4") + getChangeInBias(errO4));
        weightsAndBiases.replace("theta5", weightsAndBiases.get("theta5") + getChangeInBias(errO5));
        weightsAndBiases.replace("theta6", weightsAndBiases.get("theta6") + getChangeInBias(errO6));

//        biases.set(0, biases.get(0) + getChangeInBias(errO4));
//        biases.set(1, biases.get(1) + getChangeInBias(errO5));
//        biases.set(2, biases.get(2) + getChangeInBias(errO6));
    }
    public static double calculateAverage(List<Double> errors){
        double sumOfIndividualsFitness = getErrorsSum(errors);
        return sumOfIndividualsFitness/errors.size();
    }

    private static double getErrorsSum(List<Double> errors) {
        return errors.stream().mapToDouble(value -> value).sum();
//        return errors.stream().mapToDouble(Double::doubleValue).sum();
    }

    private static void saveToFile() {
        String filePath = "src/ann_output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list and write each object's values to a new line in the file
            int index = 0;
            for (Double predictedOutput : predictedOutputs) {
                writer.write(String.format("%.6f %.6f %.6f%n", predictedOutput, actualOutputs.get(index), errors.get(index)));
                index++;
            }
            System.out.println("Objects saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final double LEARNING_FACTOR = 0.7;

    public static void main(String[] args) {
        try {
            BufferedReader file1 = new BufferedReader(new FileReader("src/normalized_data.txt"));

              Random rand = new Random();

              //Initialize weights and biases randomly and accordingly between -1.0 and 1.0
            weightsAndBiases.put("w14",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w15",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w24",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w25",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w34",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w35",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w46",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("w56",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("theta4",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("theta5",(rand.nextDouble() * 2 - 1));
            weightsAndBiases.put("theta6",(rand.nextDouble() * 2 - 1));
            //===============
            double x1, x2, x3, y, error;
            boolean areActualOutputsSaved = false;
            while (true){//Epoch
                while (true) { // iterate over records
                    String line = file1.readLine();
                    if (line == null) break;
                    String[] values = line.split(" ");
                    x1 = Double.parseDouble(values[0]);
                    x2 = Double.parseDouble(values[1]);
                    x3 = Double.parseDouble(values[2]);
                    y = Double.parseDouble(values[3]);
                    if (!areActualOutputsSaved) actualOutputs.add(y);
                    error = forwardFeed(x1, x2, x3, y);
                    while (Math.abs(error) > 0.005) {
                        //backward <--> forward unless error threshold is met
                        backPropagate(error, x1, x2, x3);
                        error = forwardFeed(x1, x2, x3, y);
                    }

                    errors.add(Math.abs(error));
                    predictedOutputs.add(O6);
                }
                areActualOutputsSaved = true; // to save the actual values only once in the list
                if (calculateAverage(errors) <= 0.005 ) break;
                errors.clear(); // As loop is restarting, so should be the errors list
                predictedOutputs.clear(); // As loop is restarting, so should be the predicted outputs list
            }

            file1.close();

            saveToFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
