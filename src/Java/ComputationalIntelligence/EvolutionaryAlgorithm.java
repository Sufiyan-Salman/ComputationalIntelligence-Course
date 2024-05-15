package Java.ComputationalIntelligence;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class EvolutionaryAlgorithm
{
    public static double stepSize = 0.15F;
    public static int noOfChildsToBeGenerated = 10;
    public static int noOfIndividualsToBeSelected = 10;
    public static List<Individual> individuals = new ArrayList<>();
    public static List<Individual> individualsForBinary = new ArrayList<>()
            ;
    public static List<Individual> newIndividuals = new ArrayList<>()
            ;
    public static List<Individual> selectedIndividuals = new ArrayList<>();
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
    public static double xUpperBound = 20;
    public static double yUpperBound = 25;
    public static double xLowerBound = -15;
    public static double yLowerBound = -20;
    //==============================
    // ==============================
    //Que 3
//    public static double xUpperBound = 15;
//    public static double yUpperBound = 20;
//    public static double xLowerBound = -15;
//    public static double yLowerBound = -25;
    //==============================
//    public static double stepSize = 2.8;
    public static class Individual implements Comparable<Individual>, Serializable {
        double x;
        double y;
        double fitness;
        double fitnessProportion;
        int rank;
        double rankProportion;
        double commulativeProportion;
        public Individual(double x, double y, double fitness){
            this.x = x;
            this.y = y;
            this.fitness = fitness;

        }
        @Override
        public int compareTo(Individual other) {
            return Double.compare(this.fitness, other.fitness);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Individual individual = (Individual) obj;
            return ((x == individual.x) && (y == individual.y));
        }
        @Override
        public int hashCode() {
            return Objects.hash(fitness);
        }

        @Override
        public String toString() {
            return "{ x : "+x+" y : "+y+" Fitness is: "+fitness+" } \n";
        }


    }
    public void runAlgo(){
        initializeIndividuals(); //Step `1
        sortIndividuals(individuals);
        System.out.println("Starting gen: "+individuals);
        fittestIndividualsOfEachGeneration.add(individuals.get(0)); // fittest individual of first generation
        avgFitnesstOfEachGeneration.add(calculateAverage(individuals));
//        int randomIndex=0;
        while (true){
//            if(hasNegativeFitness(individuals)) makeFitnessPositive(individuals); //we make the values positive only for RP and FP

//            addCommulativeFitnessProportionOfEachIndv(individuals);

//            addCommulativeRankProportionOfEachIndv(individuals);

            individualsForBinary = new ArrayList<>(individuals);
            while (newIndividuals.size()<noOfChildsToBeGenerated) {
                //=====================
                //Selection based on fitness proportion
//                Individual individual1 = selectByFitnessProportion(individuals);
//                Individual individual2 = selectByFitnessProportion(individuals);
//                if (individual1.equals(individual2)) individual2 = selectByFitnessProportion(individuals);
                //=====================
                //Selection based on Rank proportion
//                Individual individual1 = selectByRankProportion(individuals);
//                Individual individual2 = selectByRankProportion(individuals);
//                if (individual1.equals(individual2)) individual2 = selectByRankProportion(individuals);
                //=====================
                //Selection based on binary tournament
                Individual individual1 = selectByBinaryTournament(individualsForBinary);
                individualsForBinary.remove(individual1);
                Individual individual2 = selectByBinaryTournament(individualsForBinary);
                individualsForBinary.remove(individual2);
                //=====================
                //random selection
//                Individual individual1 = individuals.get(generateRandomIndexForSelectingIndividual());
//                int indexForSecondParent = generateRandomIndexForSelectingIndividual();
//                Individual individual2 = individuals.get(indexForSecondParent);
//                if (individual1.equals(individual2)) individual2 = individuals.get((indexForSecondParent+1)%10);
                //=====================
                reproduce(individual1,individual2);
//                reproduceOneChild(individual1, individual2);
            }
            List<Individual> parentAndChildren = new ArrayList<>();
            parentAndChildren.addAll(individuals);
            parentAndChildren.addAll(newIndividuals);
            sortIndividuals(parentAndChildren);

            // select indvs via FP
//            selectedIndividuals = selectIndividualsByFitnessProportion(parentAndChildren,noOfIndividualsToBeSelected);

            // select indvs via RP
//            selectedIndividuals = selectIndividualsByRankProportion(parentAndChildren,noOfIndividualsToBeSelected);

            // selected top 10 fittest from parents and children (Truncation Strategy)
            selectedIndividuals = new ArrayList<>(parentAndChildren.subList(0,noOfIndividualsToBeSelected));

            fittestIndividualsOfEachGeneration.add(selectedIndividuals.get(0)); // fittest individual of new generation

            System.out.println("Selected New Individuals: "+selectedIndividuals);
            System.out.println("Avg fitness Individuals: "+avgFitnesstOfEachGeneration);

            avgFitnesstOfEachGeneration.add(calculateAverage(selectedIndividuals));
            if (avgFitnesstOfEachGeneration.size()>= 10 && isStoppingCriteria2Satisfied()) {
                System.out.println("Criteria Satisfied");
                break;
            }
//            if (isStoppingCriteriaSatisfied()) {
//                System.out.println("Criteria Satisfied");
//                break;
//            }
//=============================================
            //Stopping criteria 1
//            boolean allDifferencesLessThanThreshold = IntStream.range(0, selectedIndividuals.size() - 1)
//                    .allMatch(i -> Math.abs(selectedIndividuals.get(i).fitness - selectedIndividuals.get(i + 1).fitness) <= 0.001);
//            if (allDifferencesLessThanThreshold) {
//                System.out.println("Criteria Satisfied");
//                break;
//            }
//=============================================
//            if (fittestIndividualsOfEachGeneration.size()==50) {
//                System.out.println("Criteria Satisfied");
//                break;
//            }
            individuals = selectedIndividuals;
            newIndividuals.clear();

        }
        System.out.println("Fittest indv of each gen "+fittestIndividualsOfEachGeneration);
//        System.out.println("Avg fitness of each gen "+fittestIndividualsOfEachGeneration);

        // Specify the file path
        String filePath = "src/fitness_data.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list and write each object's values to a new line in the file
            for (Individual data : fittestIndividualsOfEachGeneration) {
                writer.write(String.format("%.6f %.6f %.6f%n", data.x, data.y, data.fitness));
            }
            System.out.println("Objects saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("gjfj"+loadFromFile());
    }

    private List<Individual> selectIndividualsByFitnessProportion(List<Individual> parentAndChildren, int noOfIndividualsToBeSelected) {
        List<Individual> selectedIndvs = new ArrayList<>();
        Individual selectedIndv = null;
        addCommulativeFitnessProportionOfEachIndv(individuals);
        for (int i = 0; i < noOfIndividualsToBeSelected; i++) {
            selectedIndv = selectByFitnessProportion(parentAndChildren);
//            if (selectedIndvs.contains(selectedIndv)) {
//                i--;
//                continue;
//            }else
            selectedIndvs.add(selectedIndv);
            parentAndChildren.remove(selectedIndv);
        }
        return selectedIndvs;
    }
    private List<Individual> selectIndividualsByRankProportion(List<Individual> parentAndChildren, int noOfIndividualsToBeSelected) {
        List<Individual> selectedIndvs = new ArrayList<>();
        Individual selectedIndv = null;
        addCommulativeRankProportionOfEachIndv(parentAndChildren);
        for (int i = 0; i < noOfIndividualsToBeSelected; i++) {
            selectedIndv = selectByRankProportion(parentAndChildren);
//            if (selectedIndvs.contains(selectedIndv)) {
//                i--;
//                continue;
//            }else
            selectedIndvs.add(selectedIndv);
            parentAndChildren.remove(selectedIndv);

        }
        return selectedIndvs;
    }

//    private Individual assignDifferentParent(Individual individual1) {
//        Individual individual2 = individuals.get((generateRandomIndexForSelectingIndividual()+1)%10);
//        if (individual1.equals(individual2)) {
//            System.out.println("Same parents"+individual2);
//            return assignDifferentParent(individual1);
//        }
//        return individual2;
//    }

    public void reproduce(Individual individual1, Individual individual2){
         List<Individual> newChildrenPair = doCrossOver(individual1, individual2);
         int randomValue = allowMutationBasedOnRandomNumber();
         if (randomValue!= -1){
             doMutation(randomValue, newChildrenPair.get(0));
         }
         randomValue = allowMutationBasedOnRandomNumber();
         if (randomValue!= -1){
             doMutation(randomValue, newChildrenPair.get(1));
         }
         newIndividuals.add(newChildrenPair.get(0));
         newIndividuals.add(newChildrenPair.get(1));
    }
    public void reproduceOneChild(Individual individual1, Individual individual2){
         Individual newChildrenPair = doCrossOverOneChild(individual1, individual2);
         int randomValue = allowMutationBasedOnRandomNumber();
         if (randomValue!= -1){
             doMutation(randomValue, newChildrenPair);
         }
         newIndividuals.add(newChildrenPair);
    }
    public List<Individual> doCrossOver(Individual individual1, Individual individual2){
        double fitness1 = calculateFitness(individual1.x, individual2.y);
        double fitness2 = calculateFitness(individual2.x, individual1.y);
        Individual newIndividual1 = new Individual(individual1.x, individual2.y, fitness1);
        Individual newIndividual2 = new Individual(individual2.x, individual1.y, fitness2);
//        newIndividuals.add(newIndividual1);
//        newIndividuals.add(newIndividual2);
        return List.of(newIndividual1, newIndividual2);
    }
    public Individual doCrossOverOneChild(Individual individual1, Individual individual2){
        double newChildX = (individual1.x + individual2.x)/2 ;
        double newChildY = (individual1.y + individual2.y)/2 ;
        double fitness = calculateFitness(newChildX, newChildY);
        //        newIndividuals.add(newIndividual1);
//        newIndividuals.add(newIndividual2);
        return new Individual(individual1.x, individual2.y, fitness);
    }
    public void doMutation(int randomValue, Individual individual){
        if (randomValue>50) { //mutate x
            if (randomValue%2==0) {// if even, +ve update
                individual.x+= stepSize;
                individual.x= reSetBoundary(individual.x,xLowerBound,xUpperBound);
//                individual.x= reSetXLimits(individual.x);
            }else { // odd -> -ve update
                individual.x-= stepSize;
                individual.x= reSetBoundary(individual.x,xLowerBound,xUpperBound);
//                individual.x= reSetXLimits(individual.x);
            }
        }else { //mutate y
            if (randomValue%2==0) {// if even, +ve update
                individual.y+= stepSize;
                individual.y= reSetBoundary(individual.y,yLowerBound, yUpperBound);
//                individual.y= reSetYLimits(individual.y);
            }else { // odd -> -ve update
                individual.y-= stepSize;
                individual.y= reSetBoundary(individual.y,yLowerBound, yUpperBound);
//                individual.y= reSetYLimits(individual.y);
            }
        }
    }
    public int generateRandomIndexForSelectingIndividual(int upperBound) {
        // Modify the logic based on your specific conditions for allowing mutation
       return ThreadLocalRandom.current().nextInt(0, upperBound);
    }
    public int allowMutationBasedOnRandomNumber() {
    int randomNumber = ThreadLocalRandom.current().nextInt(1, 401);
    // Modify the logic based on your specific conditions for allowing mutation
        if (randomNumber>=1 && randomNumber<=100) return randomNumber;
        else return -1;
    }

    public boolean isElementWithinLimits(double elementToBeChecked, double lowerBound, double upperBound){
        // constraint
        return elementToBeChecked>= lowerBound && elementToBeChecked<= upperBound ;
    }

    public double reSetBoundary(double elementToBeChecked, double lowerBoundary, double upperBoundary){
        // boundary checking
        if (!isElementWithinLimits(elementToBeChecked, lowerBoundary, upperBoundary)) {
            if (elementToBeChecked > upperBoundary) elementToBeChecked = upperBoundary;
            else if (elementToBeChecked < lowerBoundary) elementToBeChecked = lowerBoundary;
        }
        return elementToBeChecked ;
    }
    public double calculateFitness(double x, double y){

//        return (100-Math.pow((x-(7*y)),2)+(8*Math.pow(y,2))-(6*x)); // Que 3 // FP
        return ((-7*(Math.pow(x,2)))+(3*x*(Math.sin(y)))-(786*y)+989); // Que 2
//        return (double) ((11*x)-(7.59*y)); // Que 1
    }
    public double calculateAverage(List<Individual> individuals){
        double sumOfIndividualsFitness = getSumOfIndividualsFitness(individuals);
        return sumOfIndividualsFitness/individuals.size();
    }

    private static double getSumOfIndividualsFitness(List<Individual> individuals) {
        return individuals.stream().mapToDouble(individual -> individual.fitness).sum();
    }
    private static double getSumOfIndividualsRank(List<Individual> individuals) {
        return individuals.stream().mapToInt(individual -> individual.rank).sum();
    }


    public void initializeIndividuals(){
        for (int i = 0; i < noOfIndividualsToBeSelected ; i++) {
            individuals.add(initializeIndividual());
        }
    }
    public Individual initializeIndividual(){
//        ThreadLocalRandom.current().setSeed(123456789).;
        double x =  ThreadLocalRandom.current().nextDouble(xLowerBound, xUpperBound + 0.01); // lower bound is inclusive, upper bound is exclusive
        double y =  ThreadLocalRandom.current().nextDouble(yLowerBound, yUpperBound + 0.01);
        double fitness = calculateFitness(x,y);
        return new Individual(x,y,fitness);
    }
    public boolean isStoppingCriteria1Satisfied(){

        double averageOfIndividuals;
        double averageOfNewIndividuals;
        double total=0;
        for (Individual individual: individuals){
            total+=individual.fitness;
        }
        averageOfIndividuals = total/individuals.size();
        for (Individual individual: selectedIndividuals){
            total+=individual.fitness;
        }
        averageOfNewIndividuals = total/selectedIndividuals.size();
        return averageOfIndividuals == averageOfNewIndividuals;
    }
    public boolean isStoppingCriteria2Satisfied(){
        int starttIndex = avgFitnesstOfEachGeneration.size()-10; // so thAt we always start with last 10
        for (; starttIndex < avgFitnesstOfEachGeneration.size()-2 ; starttIndex++){
            if (Math.abs(avgFitnesstOfEachGeneration.get(starttIndex) - avgFitnesstOfEachGeneration.get(starttIndex+1))>0.001) return  false;
        }
        return true;
    }
    public void sortIndividuals(List<Individual> individuals){
        Collections.sort(individuals, Collections.reverseOrder()); //descending
    }
    public boolean hasNegativeFitness(List<Individual> individuals){
        for (Individual individual: individuals) {
            if (individual.fitness < 0) return true;
        }
        return false;

    }
    public void makeFitnessPositive(List<Individual> individuals){
        Individual smallestFitnessIndv = Collections.min(individuals);
        double valueToBeAdded = Math.abs(smallestFitnessIndv.fitness)+1;
        individuals.forEach(individual -> individual.fitness += valueToBeAdded);

    }
    public void addFitnessProportionOfEachIndv(List<Individual> individuals){
        double totalFitness = getSumOfIndividualsFitness(individuals);
        individuals.forEach(individual -> {
            individual.fitnessProportion = individual.fitness / totalFitness;
        });
    }

    private void addRankOfEachIndv(List<Individual> individuals) {
        sortIndividuals(individuals);
        AtomicInteger rank = new AtomicInteger(individuals.size());
        individuals.forEach(individual -> {
            individual.rankProportion = rank.get();
            rank.getAndDecrement();
        });
    }
    private void addRankProportionOfEachIndv(List<Individual> individuals) {
        addRankOfEachIndv(individuals);
        double totalRank = getSumOfIndividualsRank(individuals);
        individuals.forEach(individual -> {
            individual.rankProportion = individual.rank / totalRank;
        });
    }
    public void addCommulativeRankProportionOfEachIndv(List<Individual> individuals){
        addRankProportionOfEachIndv(individuals);
        AtomicReference<Double> cFP = new AtomicReference<>((double) 0);
        individuals.forEach(individual -> {
            individual.commulativeProportion = individual.rankProportion+ cFP.get();
            cFP.set(individual.commulativeProportion);
        });
    }
    public void addCommulativeFitnessProportionOfEachIndv(List<Individual> individuals){
        addFitnessProportionOfEachIndv(individuals);
        AtomicReference<Double> cFP = new AtomicReference<>((double) 0);
        individuals.forEach(individual -> {
            individual.commulativeProportion = individual.fitnessProportion+ cFP.get();
            cFP.set(individual.commulativeProportion);
        });
    }
    // add FP in Indv object as well as cp
    public Individual selectByBinaryTournament(List<Individual> individuals) {
        Individual individual1 = individuals.get(generateRandomIndexForSelectingIndividual(individuals.size()));
        int indexForSecondParent = generateRandomIndexForSelectingIndividual(individuals.size());
        Individual individual2 = individuals.get(indexForSecondParent);
        if (individual1.fitness > individual2.fitness) return individual1;
        else return individual2;

    }
    public Individual selectByFitnessProportion(List<Individual> individuals){

        double lowerBound = 0;
        double random = ThreadLocalRandom.current().nextDouble(0, individuals.get(individuals.size()-1).commulativeProportion + 0.00000001); // lower bound is inclusive, upper bound is exclusive
        for (Individual individual: individuals){
            if (random >lowerBound && random<= individual.commulativeProportion) return  individual;
            lowerBound = individual.commulativeProportion;
        }
        return null;
    }
    public Individual selectByRankProportion(List<Individual> individuals){

        double lowerBound = 0;
        double random = ThreadLocalRandom.current().nextDouble(0, individuals.get(individuals.size()-1).commulativeProportion + 0.00000001); // lower bound is inclusive, upper bound is exclusive
        for (Individual individual: individuals){
            if (random >lowerBound && random<= individual.commulativeProportion) return  individual;
            lowerBound = individual.commulativeProportion;
        }
        return null;
    }
}
