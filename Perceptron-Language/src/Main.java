import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Main {


    static ArrayList<String> name = new ArrayList<>();
    static ArrayList<ArrayList<Double>> normilizedVector = new ArrayList<>();

    static ArrayList<Perceptron> perceptronList;
    static String[] directories;
    static int y;

    public static void main(String[] args) {
        y = 0;

        perceptronList = new ArrayList<>();
        File file = new File("C:\\Users\\Ян Региневич\\Desktop\\NAI\\Perceptron-Language\\Languages");
        directories = file.list((dir, name) -> new File(dir, name).isDirectory());

        assert directories != null;
        for (String s : directories) {
            perceptronList.add(new Perceptron(s));
            System.out.println(s);
        }



        for (String directory : directories) {
            try {
                readFolderData("C:\\Users\\Ян Региневич\\Desktop\\NAI\\Perceptron-Language\\Languages\\" + directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < name.size(); i++) {
            System.out.print(name.get(i) + "  ");
            for (int j = 0; j < normilizedVector.get(i).size(); j++) {
                System.out.println(normilizedVector.get(i).get(j));
            }
            System.out.println();
        }
        for (int i = 0; i < directories.length; i++) {
            Study(0, 0);
        }
        languageCheck();
    }

    public static void Study(int i, int j) {
        for (; i < perceptronList.size(); i++) {
            double net = 0;
            for (; j < normilizedVector.size(); j++) {
                for (int k = 0; k < normilizedVector.get(j).size(); k++) {
                    net += normilizedVector.get(j).get(k) * perceptronList.get(i).getWagi().get(k);
                }
                if (net < perceptronList.get(i).getT() && name.get(j).equals(perceptronList.get(i).getLanguage())) {
                    y = 0;
                    obliczanie(i, j);
                    System.out.println("FALSE " + i + " : " + j);
                    Study(i, j);
                    return;
                } else {
                    if (net > perceptronList.get(i).getT() && !name.get(j).equals(perceptronList.get(i).getLanguage())) {
                        y = 1;
                        obliczanie(i, j);
                        System.out.println("FALSE " + i + " : " + j);
                        Study(i, j);
                        return;
                    } else System.out.println("OK");
                }
            }
        }
    }
    public static void languageCheck() {
        HashMap<Perceptron, Double> perceptronMap = new HashMap<>();
        BufferedReader reader =new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Print your word: ");
        try {
            String word = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Perceptron perceptron : perceptronList) {
            double net = 0;
            for (ArrayList<Double> doubles : normilizedVector) {
                for (int k = 0; k < doubles.size(); k++) {
                    net += doubles.get(k) * perceptron.getWagi().get(k);
                }
                if (net > perceptron.getT()) {
                    perceptronMap.put(perceptron, net);
                }
            }
        }
        double MaxValueInMap = (Collections.max(perceptronMap.values()));
        for (Map.Entry<Perceptron, Double> values : perceptronMap.entrySet()){
                if (values.getValue() == MaxValueInMap)
                {
                    System.out.println(values.getKey().language);
                }
        }

    }
    public static void obliczanie(int i, int j) {
        int d;
        if (y == 0) {
            d = 1;
        } else {
            d = 0;
        }
        ArrayList<Double> list = new ArrayList<>();
        double helpValue = (d - y) * perceptronList.get(i).alpha;
        System.out.println("HELP VALUSE"+helpValue);
        normilizedVector.get(j).add((double) -1);
        System.out.println(normilizedVector.get(j));
        for (int k = 0; k < normilizedVector.get(j).size(); k++) {
            list.add(helpValue * normilizedVector.get(j).get(k));
        }
        perceptronList.get(i).wagi.add(perceptronList.get(i).t);
        for (int k = 0; k < perceptronList.get(i).wagi.size(); k++) {
            perceptronList.get(i).wagi.set(k, perceptronList.get(i).wagi.get(k) + list.get(k));
        }
        System.out.println("DO ZMIANY "+perceptronList.get(i).t);
        perceptronList.get(i).t=perceptronList.get(i).wagi.get(perceptronList.get(i).wagi.size()-1);
        System.out.println("PO ZMIANIE "+perceptronList.get(i).t);
        perceptronList.get(i).wagi.remove(perceptronList.get(i).wagi.size() - 1);
        normilizedVector.get(j).remove(normilizedVector.get(j).size() - 1);
    }
    public static void readFolderData(String directory) throws IOException {
        File file = new File(directory);
        File[] listOfFiles = file.listFiles();
        String str;
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            str = "";
            String path = String.valueOf(Paths.get(String.valueOf(listOfFiles[i])));
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String string = reader.readLine();
            while (string != null) {
                str += string;
                string = reader.readLine();
            }
            str = str.replaceAll("[^a-z]", "");
            name.add(file.getName());
            normilizedVector.add(countChars(str));
        }
    }
    public static ArrayList<Double> countChars(String str) {
        Map<Character, Integer> textMap = new TreeMap<>();
        for (int i = 0; i < 26; i++) {
            textMap.put((char) ('a' + i), 0);
        }
        int count;
        for (int i = 0; i < str.length(); i++) {
            count = 0;
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j)) {
                    count++;
                }
            }
            textMap.put(str.charAt(i), count);
        }
        ArrayList<Double> list = new ArrayList<>();
        for (Map.Entry<Character, Integer> li : textMap.entrySet()) {
            list.add(Double.valueOf(li.getValue()));
        }
        double sum = 0;
        for (Double aDouble : list) {
            sum += Math.pow(aDouble, 2);
        }
        sum = Math.sqrt(sum);
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i) / sum);
        }
        return list;
    }
}