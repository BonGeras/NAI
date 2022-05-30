import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NaiveBayes {
    static ArrayList<ArrayList<String>>testList;
    static ArrayList<ArrayList<String>>trainList;
    static double countTak;
    static double countNie;

    public static void DataFill(ArrayList<ArrayList<String>> testList, ArrayList<ArrayList<String>> trainList) throws FileNotFoundException {
        File testSetFile = new File("C:\\Users\\Ян Региневич\\Desktop\\Test.txt");
        File trainSetFile = new File("C:\\Users\\Ян Региневич\\Desktop\\Train.txt");
        Scanner scannerTestFile = new Scanner(testSetFile);
        Scanner scannerTrainFile = new Scanner(trainSetFile);
        while (scannerTestFile.hasNextLine()) {
            String dataLine = scannerTestFile.nextLine();
            String[] dataArray = dataLine.split(",");
            ArrayList<String> dataArrayList = new ArrayList<>(Arrays.asList(dataArray));
            testList.add(dataArrayList);
        }
        while (scannerTrainFile.hasNextLine()) {
            String dataLine = scannerTrainFile.nextLine();
            String[] dataArray = dataLine.split(",");
            ArrayList<String> dataArrayList = new ArrayList<>(Arrays.asList(dataArray));
            trainList.add(dataArrayList);
        }
    }

    public static int counterTak(String parameter, int k) {
        int count = 0;
        for (ArrayList<String> strings : trainList) {
            if (strings.get(k).equals(parameter) && strings.get(strings.size() - 1).equals("tak")) count++;
        }
        System.out.print("True: " + parameter + "-" + count + " ");
        return count;
    }

    public static int counterNie(String str, int k) {
        int count = 0;
        for (ArrayList<String> strings : trainList) {
            if (strings.get(k).equals(str) && strings.get(strings.size() - 1).equals("nie")) count++;
        }
        System.out.println("False: "+str+"-"+count);
        return count;
    }

    public static String selectDecision(double probabilityForTak, double probabilityForNie){
        String answear;
        if (probabilityForTak > probabilityForNie) answear = "tak";
        else answear = "nie";
        return answear;
    }

    public static double smoothing(double counter, double countYN, int k) {
        counter += 1;
        double total;
        Set<String> set = new HashSet<>();
        for (ArrayList<String> strings : trainList) {
            set.add(strings.get(k));
        }
        System.out.println(set);
        total = counter / (countYN + set.size());
        System.out.println("After smoothing value become: ["+total+"]");
        return total;
    }

    public static void Testing() {
        countTak = 0;
        countNie = 0;
        for (ArrayList<String> strings : trainList) {
            if (strings.get(strings.size() - 1).equals("nie")) countNie++;
            else countTak++;
        }
        double takRate = 0;
        double nieRate = 0;
        for (int i = 0; i < testList.size(); i++) {
            takRate += countTak / trainList.size();
            nieRate += countNie / trainList.size();
            for (int k = 0; k < testList.get(k).size(); k++) {
                double takCounter = counterTak(testList.get(i).get(k), k);
                if (takCounter == 0) {
                    double newTmp = smoothing(takCounter, countTak, k);
                    takRate = takRate * newTmp;
                } else takRate = takRate * (takCounter / countTak);
                double tmpF = counterNie(testList.get(i).get(k), k);
                if (tmpF == 0) {
                    double newTmp = smoothing(tmpF, countNie, k);
                    nieRate = nieRate * newTmp;
                } else nieRate = nieRate * (tmpF / countNie);
            }
            System.out.println("FOR ROUND: " + i + "\n" + "TRUE RATE: " + takRate + "\nFALSE RATE: " + nieRate);
            System.out.println("Decision: " + selectDecision(takRate, nieRate));
            System.out.println("+--------------------------------------------+");

            takRate = 0;
            nieRate = 0;
        }
        System.out.print("Enter your own values: ");
        Scanner scanner = new Scanner(System.in);
        String given = scanner.nextLine();
        String[] help = given.split(",");
        ArrayList<String> givenList = new ArrayList<>(Arrays.asList(help));
        takRate += countTak / trainList.size();
        nieRate += countNie / trainList.size();
        for (int j = 0; j < givenList.size(); j++) {
            double takCount = counterTak(givenList.get(j), j);
            if (takCount == 0) {
                double newTmp = smoothing(takCount, countTak, j);
                takRate = takRate * newTmp;
            } else {
                takRate = takRate * (takCount / countTak);
            }
            double nieCount = counterNie(givenList.get(j), j);
            if (nieCount == 0) {
                double newTmp = smoothing(nieCount, countNie, j);
                nieRate = nieRate * newTmp;
            } else {
                nieRate = nieRate * (nieCount / countNie);
            }
        }
        System.out.println("GIVEN DATA TRUE RATE: " + takRate + "\nGIVEN DATA FALSE RATE: " + nieRate);
        System.out.println("Decision: " + selectDecision(takRate, nieRate));
    }

    public static void main(String[] args) throws FileNotFoundException {
        testList = new ArrayList<>();
        trainList = new ArrayList<>();
        DataFill(testList, trainList);
        Testing();
    }
}
