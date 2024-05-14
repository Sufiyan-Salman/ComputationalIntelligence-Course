package Java.ComputationalIntelligence;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PSOAlgorithm {

    static final double C1 = 2;
    static final double C2 = 2;
    static final double LOWER_X = -5;
    static final double UPPER_X = 5;
    static final double LOWER_Y = -1;
    static final double UPPER_Y = 20;

    static Random rand = new Random();
    static List<Double> avgFitnessOfEachIteration = new ArrayList<>();
    static List<Double> gBestOfEachIteration = new ArrayList<>();

    static class Individual {
        double x;
        double y;
        double vx;
        double vy;
        double fitness;
        Individual pbest;


        Individual(double x, double y , Individual pbest, double fitness) {
            this.x = x ;
            this.y = y;
            this.vx = 0;
            this.vy = 0;
            this.pbest = pbest;
            this.fitness = fitness;
        }

        @Override
        public String toString() {
            return "Individual{" +
                    "x=" + x +
                    ", y=" + y +
                    ", vx=" + vx +
                    ", vy=" + vy +
                    ", fitness=" + fitness +
                    "}\n";
        }
    }
    public static void initializeIndividuals(List<Individual> individuals){
        for (int i = 0; i < 10 ; i++) {
            individuals.add(initializeIndividual());
        }
    }
    public static Individual initializeIndividual(){
        double x =  ThreadLocalRandom.current().nextDouble(LOWER_X, UPPER_X + 0.01); // lower bound is inclusive, upper bound is exclusive
        double y =  ThreadLocalRandom.current().nextDouble(LOWER_Y, UPPER_Y + 0.01);
        double fitness = calculateFitness(x,y);
        Individual initialPbest = new Individual(0,0,null,0);
        return new Individual(x,y,initialPbest,fitness);
    }

    static double calculateFitness(double x, double y ) {
        return (100 - Math.pow(x, 2)) + (Math.pow(y, 2) - 56 * x * y) - Math.pow(Math.sin(x / 2), 2);
    }

    static Individual updateVelocityAndPosition(Individual I, Individual gbest) {
        I.vx = I.vx + C1 * Math.random() * (I.pbest.x - I.x) + C2 * Math.random() * (gbest.x - I.x);
        I.vy = I.vy + C1 * Math.random() * (I.pbest.y - I.y) + C2 * Math.random() * (gbest.y - I.y);
        I.x = Math.max(LOWER_X, Math.min(UPPER_X, I.x + I.vx)); // bounds checking
        I.y = Math.max(LOWER_Y, Math.min(UPPER_Y, I.y + I.vy)); // bounds checking
        return I;

    }

    static Individual calculatePbest(Individual I) {
        if (I.pbest.fitness < I.fitness) {
            I.pbest = I;
        }
        return I;
    }

    static Individual findGlobalBest(List<Individual> Iarr) {
        Individual globalBest = Iarr.get(0);
        for (int i = 1; i < Iarr.size(); i++) {
            if (Iarr.get(i).fitness > globalBest.fitness) {
                globalBest = Iarr.get(i);
            }
        }
        return globalBest;
    }

    static boolean checkFitnessDifferenceZero(List<Individual> Iarr, Individual gBest,int step) {
        if (step == 1) {
            return true;
        }
        double allIndividualsFitnessSum = 0;
        for (int i = 0; i < 10; i++) {
            allIndividualsFitnessSum += Iarr.get(i).fitness;
        }
        double avgFitness = allIndividualsFitnessSum / 10;
        double difference = Math.abs(avgFitness - gBest.fitness);
        return difference > Double.MIN_VALUE;
    }

    public static double calculateAverage(List<Individual> individuals){
        double sumOfIndividualsFitness = getSumOfIndividualsFitness(individuals);
        return sumOfIndividualsFitness/individuals.size();
    }

    private static double getSumOfIndividualsFitness(List<Individual> individuals) {
        return individuals.stream().mapToDouble(individual -> individual.fitness).sum();
    }
    static void psoAlgorithm(){
        List<Individual> individualArray = new ArrayList<>();

        initializeIndividuals(individualArray);

        Individual gbest = findGlobalBest(individualArray); // can be refactored

        int step = 1;
        while (checkFitnessDifferenceZero(individualArray,gbest, step)) {
            System.out.println("-----------------------------STEP NO :" + step + "-----------------------------");
            // re calculate fitness and Pbest
            for (Individual I : individualArray) {
                I.fitness = calculateFitness(I.x, I.y);
                I.pbest = calculatePbest(I);
            }
            gbest = findGlobalBest(individualArray);
            avgFitnessOfEachIteration.add(calculateAverage(individualArray));
            gBestOfEachIteration.add(gbest.fitness);
            calculateAverage(individualArray);
            for (Individual I : individualArray) {
                updateVelocityAndPosition(I, gbest); // updating velocity and position of each indv
            }
            System.out.println(individualArray);
            step++;
        }
            System.out.println("Avg fitness of each iteration: "+ avgFitnessOfEachIteration);
            System.out.println("Gbest fitnses of each iteration: "+ gBestOfEachIteration);
        // Specify the file path
        String filePath = "src/pso_data.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list and write each object's values to a new line in the file
            int index = 0;
            for (Double avgFitness : avgFitnessOfEachIteration) {
                writer.write(String.format("%.6f %.6f%n", avgFitness, gBestOfEachIteration.get(index)));
                index++;
            }
            System.out.println("Objects saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        psoAlgorithm();;

    }
}
