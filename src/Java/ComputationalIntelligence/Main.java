package Java.ComputationalIntelligence;

import java.io.UnsupportedEncodingException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
        public static String value = "";
    public static void main(String[] args) throws UnsupportedEncodingException {
        // Press Ctrl+. with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm();
//        EvolutionaryAlgorithmKnapSack evolutionaryAlgorithm = new EvolutionaryAlgorithmKnapSack();
        evolutionaryAlgorithm.runAlgo();
//        SwingPaint.createAndShowGUI();
      /*
        System.out.println("Hello and welcome!");



//        String inputString = input.nextLine();  // Read user input
        String inputString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" ;  // Read user input
        String inputString2 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";  // Read user input
        StringBuilder divisor = new StringBuilder();
        System.out.println(inputString.length());
        System.out.println(inputString2.length());
        int currentIndex = 0;
        for (Character charOfString:  inputString.toCharArray()  ) {
                divisor.append(charOfString);
                if (divisor.length()*2 > inputString.length()) break;
                if ( isNextSame(inputString.substring(currentIndex+1,currentIndex+divisor.length()+1), String.valueOf(divisor))){
                    if (isDivides(divisor.toString(),inputString)) {
                        if (isDivides(divisor.toString(), inputString2))  setDivisor(divisor);
                    }
                }
                currentIndex++;
        }
        System.out.println(value.length());
        System.out.println(value);
    }

    private static void setDivisor(StringBuilder divisor) {
        if (value.length() < divisor.length()) value = divisor.toString();
    }

    public static boolean isDivides(String divisor, String dividend){
        if (dividend.length() % divisor.length() != 0) return false;
        dividend = dividend.replace(divisor,"0");
        try {
            return Integer.parseInt(dividend)==0;

        }catch (Exception e) {
            return false;
        }
    }
    public static boolean isNextSame(String toBeCheckString, String givenStr){
        return toBeCheckString.equals(givenStr);
*/
    }
}
