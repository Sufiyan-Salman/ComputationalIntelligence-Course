package Java.ComputationalIntelligence;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EvolutionaryAlgorithmKnapSack
{//for knapsack problem
//    public static double stepSize = 0.15F;
    public static int noOfChildrenToBeGenerated = 10;
    public static int noOfIndividualsToBeSelected = 10;
    public static List<Individual> individuals = new ArrayList<>()
            ;
    public static List<Item> availableItems = new ArrayList<>()
            ;
    public static List<Individual> newIndividuals = new ArrayList<>()
            ;
    public static List<Individual> selectedItems = new ArrayList<>();
    public static List<Individual> fittestIndividualsOfEachGeneration = new ArrayList<>()
            ;
    public static List<Double> avgFitnesstOfEachGeneration = new ArrayList<>()
            ;
    //==============================
    //Que 1
//    public static double xUpperBound = 7.5;
//    public static double yUpperBound = 2.85;
//    public static double xLowerBound = 0.5;
//    public static double yLowerBound = -4.0;
    //==============================
    //Que 2
//    public static double xUpperBound = 20;
//    public static double yUpperBound = 25;
//    public static double xLowerBound = -15;
//    public static double yLowerBound = -20;
    //==============================
//    public static double stepSize = 2.8;
    public static double maxWeightLimit= 18;
    public static class Individual implements Comparable<Individual>, Serializable {
        //THis individual shows possible solution of selected item
        double weight;
        double worth;
        List<Boolean> pickedItems = new ArrayList<>();

        public Individual(){
        }
        @Override
        public int compareTo(Individual other) {
            return Double.compare(this.calculateTotalWorth(), other.calculateTotalWorth());
//            return Double.compare(calculateTotalWorth(this.pickedItems),calculateTotalWorth(other.pickedItems));
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Individual individual = (Individual) obj;
            return ((pickedItems.equals(individual.pickedItems)));
        }
        @Override
        public int hashCode() {
            return Objects.hash(worth);
        }

        @Override
        public String toString() {
            return "{ Selected bits: "+ pickedItems+" weight : "+calculateTotalWeight()+" Worth is: "+calculateTotalWorth()+" } \n";
//            return "{ item : "+item+" weight : "+weight+" Worth is: "+worth+" } \n";
        }
        public double calculateTotalWorth(){
//    public double calculateTotalWorth(List<Individual> items){
            double worth = 0.0;
            int indexOfItem = 0;
            for (boolean present: pickedItems){
                if (present) worth += availableItems.get(indexOfItem).worth;
                indexOfItem++;
            }
            return worth;
        }
        public double calculateTotalWeight(){
//    public double calculateTotalWorth(List<Individual> items){
            double worth = 0.0;
            int indexOfItem = 0;
            for (boolean present: pickedItems){
                if (present) worth += availableItems.get(indexOfItem).weight;
                indexOfItem++;
            }
            return worth;
        }


    }
    public static class Item implements Comparable<Item>, Serializable {
//        int item; //thats useless
        double weight;
        double worth;
        public Item(/*int item,*/ double weight, double worth){
//            this.item = item;
            this.weight = weight;
            this.worth = worth;

        }
        @Override
        public int compareTo(Item other) {
            return Double.compare(this.worth, other.worth);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Item individual = (Item) obj;
            return (/*(item == individual.item) && */ (weight == individual.weight));
        }
        @Override
        public int hashCode() {
            return Objects.hash(worth);
        }

        @Override
        public String toString() {
            return "{ weight : "+weight+" Worth is: "+worth+" } \n";
//            return "{ item : "+item+" weight : "+weight+" Worth is: "+worth+" } \n";
        }


    }
    public void runAlgo(){
        setItemsToBeSelectedForKnapSack();
        initializeIndividuals(); //Step `1
        sortIndividuals(individuals);
        System.out.println("Starting gen: "+individuals);
        fittestIndividualsOfEachGeneration.add(individuals.get(0)); // fittest individual of first generation
        avgFitnesstOfEachGeneration.add(calculateAverage(individuals));
//        int randomIndex=0;
        while (true){

            while (newIndividuals.size()< noOfChildrenToBeGenerated) {
                Individual individual1 = individuals.get(generateRandomIndexForSelectingIndividual());
                int indexForSecondParent = generateRandomIndexForSelectingIndividual();
                Individual individual2 = individuals.get(indexForSecondParent);
                if (individual1.equals(individual2)) individual2 = individuals.get((indexForSecondParent+1)%10);
//                if (individual1.equals(individual2)) individual2 = assignDifferentParent(individual1);
                reproduce(individual1, individual2);
            }
            List<Individual> parentAndChildren = new ArrayList<>();
            parentAndChildren.addAll(individuals);
            parentAndChildren.addAll(newIndividuals);
            sortIndividuals(parentAndChildren);
            // selected top 10 fittest from parents and children
            selectedItems = new ArrayList<>(parentAndChildren.subList(0,noOfIndividualsToBeSelected));
            fittestIndividualsOfEachGeneration.add(selectedItems.get(0)); // fittest individual of new generation
            System.out.println("Selected New Individuals: "+selectedItems);
            System.out.println("Avg fitness Individuals: "+avgFitnesstOfEachGeneration);
            avgFitnesstOfEachGeneration.add(calculateAverage(selectedItems));
            if (avgFitnesstOfEachGeneration.size()>= 10 && isStoppingCriteria2Satisfied()) {
                System.out.println("Criteria Satisfied");
                break;
            }

            individuals = selectedItems;
            newIndividuals.clear();

        }
        System.out.println("Fittest indv of each gen "+fittestIndividualsOfEachGeneration);
        System.out.println("Avg fitness of each gen "+avgFitnesstOfEachGeneration);

        // Specify the file path
        String filePath = "fitness_data.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list and write each object's values to a new line in the file
            for (Individual data : fittestIndividualsOfEachGeneration) {
                writer.write(String.format("%.6f %.6f%n", data.calculateTotalWeight(), data.calculateTotalWorth()));
//                writer.write(String.format("%.6f %.6f %.6f%n", data.x, data.y, data.fitness));
            }
            System.out.println("Objects saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("gjfj"+loadFromFile());
    }

    private Individual assignDifferentParent(Individual individual1) {
        Individual individual2 = individuals.get((generateRandomIndexForSelectingIndividual()+1)%10);
        if (individual1.equals(individual2)) {
            System.out.println("Same parents"+individual2);
            return assignDifferentParent(individual1);
        }
        return individual2;
    }

    private static List<Individual> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("fitness_data.txt"))) {
            return (List<Individual>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    public static void readItems(String fileName) {
        List<Item> items = new ArrayList<>();
        int capacity = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) { // Assuming weight and worth are separated by a space
                    int weight = Integer.parseInt(parts[0]);
                    int worth = Integer.parseInt(parts[1]);
                    items.add(new Item(weight, worth));
                } else if (parts.length == 1) { // Assuming the last line represents the capacity
                    maxWeightLimit = Integer.parseInt(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        availableItems = items;
//        return new Knapsack(items, capacity);
    }

    public void reproduce(Individual individual1, Individual individual2){
        List<Individual> newChildrenPair = doUniformCrossOver(individual1, individual2);
        int randomValue = allowMutationBasedOnRandomNumber();
        if (newChildrenPair != null){
            if (randomValue != -1) {
                doMutationBitFlip(randomValue, newChildrenPair.get(0));
            }
            randomValue = allowMutationBasedOnRandomNumber();
            if (randomValue != -1) {
                doMutationBitFlip(randomValue, newChildrenPair.get(1));
            }
            newIndividuals.add(newChildrenPair.get(0));
            newIndividuals.add(newChildrenPair.get(1));
        }
    }

    public List<Individual> doNPointCossOver(Individual individual1, Individual individual2){

        Individual newIndividual1= new Individual();
        for (int i = 0; i < individual1.pickedItems.size(); i++) {
            if (i%2 == 0) newIndividual1.pickedItems.add(individual1.pickedItems.get(i));
            else newIndividual1.pickedItems.add(individual2.pickedItems.get(i));
            if (!isElementWithinLimits(newIndividual1)){
                return null;
            }
        }
        Individual newIndividual2= new Individual();
        for (int i = 0; i < individual2.pickedItems.size(); i++) {
            if (i%2 == 0) newIndividual2.pickedItems.add(individual2.pickedItems.get(i));
            else newIndividual2.pickedItems.add(individual1.pickedItems.get(i));
            if (!isElementWithinLimits(newIndividual2)){
                return null;
            }
        }

        newIndividuals.add(newIndividual1);
        newIndividuals.add(newIndividual2);
        return List.of(newIndividual1, newIndividual2);
    }
    public List<Individual> doUniformCrossOver(Individual individual1, Individual individual2){
        Individual newIndividual1= new Individual();
        double random;
        for (int i = 0; i < individual1.pickedItems.size(); i++) {
            random = generateRandomDoubleForUniformCrossover();
            if (random>=0 && random<=0.5) newIndividual1.pickedItems.add(individual1.pickedItems.get(i));
            else if (random>0.5 && random<=1) newIndividual1.pickedItems.add(individual2.pickedItems.get(i));
            if (!isElementWithinLimits(newIndividual1)){
                return null;
            }
        }
        Individual newIndividual2= new Individual();
        for (int i = 0; i < individual1.pickedItems.size(); i++) {
            random = generateRandomDoubleForUniformCrossover();
            if (random>=0 && random<=0.5) newIndividual2.pickedItems.add(individual1.pickedItems.get(i));
            else if (random>0.5 && random<=1) newIndividual2.pickedItems.add(individual2.pickedItems.get(i));
            if (!isElementWithinLimits(newIndividual2)){
                return null;
            }
        }
        newIndividuals.add(newIndividual1);
        newIndividuals.add(newIndividual2);
        return List.of(newIndividual1, newIndividual2);
    }
    public void doMutationBitFlip(int randomValue, Individual individual){
        //Bit flipping
        int random =generateRandomIndexForSelectingIndividual();
        if (randomValue>50) { //mutate x
            individual.pickedItems.set(random, true);
            if (!isElementWithinLimits(individual))  individual.pickedItems.set(random, false);

        }else {
            individual.pickedItems.set(random, false);
            if (!isElementWithinLimits(individual))  individual.pickedItems.set(random, true);
        }
    }
    public void doMutationBitsSwap(int randomValue, Individual individual){

        int random1 =generateRandomIndexForSelectingIndividual();
        int random2 =generateRandomIndexForSelectingIndividual();
        boolean bitAtRandom1 = individual.pickedItems.get(random1);
        boolean bitAtRandom2 = individual.pickedItems.get(random2);
        individual.pickedItems.set(random1, bitAtRandom2);
        individual.pickedItems.set(random2, bitAtRandom1);
        if (!isElementWithinLimits(individual)) {
            individual.pickedItems.set(random1, bitAtRandom1);
            individual.pickedItems.set(random2, bitAtRandom2);
        }

    }
    public int generateRandomIndexForSelectingIndividual() {
        // Modify the logic based on your specific conditions for allowing mutation
       return ThreadLocalRandom.current().nextInt(0, 10);
    }
    public double generateRandomDoubleForUniformCrossover() {
        // Modify the logic based on your specific conditions for allowing mutation
       return ThreadLocalRandom.current().nextDouble(0.0, 1.00001);
    }
    public int allowMutationBasedOnRandomNumber() {
    int randomNumber = ThreadLocalRandom.current().nextInt(1, 401);
    // Modify the logic based on your specific conditions for allowing mutation
        if (randomNumber>=1 && randomNumber<=100) return randomNumber;
        else return -1;
    }

    public boolean isElementWithinLimits(Individual individual){
        // constraint
        return individual.calculateTotalWeight()<=maxWeightLimit ;
    }

    public double calculateAverage(List<Individual> individuals){
        double sumOfIndividualsFitness = individuals.stream().mapToDouble(Individual::calculateTotalWorth).sum();
        return sumOfIndividualsFitness/individuals.size();
    }

    public void initializeIndividuals(){
        for (int i = 0; i < noOfIndividualsToBeSelected ; i++) {
            individuals.add(initializeIndividual());
        }
    }
    public Individual initializeIndividual(){

        Individual individual = new Individual();
        Random random = new Random();

        // Generate a random boolean value
        boolean randomBoolean;
        for (int i=0 ; i<availableItems.size(); i++){
            randomBoolean = random.nextBoolean();
            individual.pickedItems.add(randomBoolean);
            System.out.println("Random bool: "+randomBoolean);
            if (!isElementWithinLimits(individual)) individual.pickedItems.set(i,false);

        }
        return individual;

    }

    public boolean isStoppingCriteria2Satisfied(){
        int starttIndex = avgFitnesstOfEachGeneration.size()-10; // so thAt we always start with last 10
        for (; starttIndex < avgFitnesstOfEachGeneration.size()-2 ; starttIndex++){
            if (Math.abs(avgFitnesstOfEachGeneration.get(starttIndex) - avgFitnesstOfEachGeneration.get(starttIndex+1))>0.001) return  false;
        }
        return true;
    }
    public void sortIndividuals(List<Individual> individuals){
        Collections.sort(individuals, Collections.reverseOrder());
    }
    public void setItemsToBeSelectedForKnapSack(){
        //read from txt file
        readItems("/home/sufiyan/Desktop/Idea Projects/Learning/problem knapsack.txt");
    }
}
