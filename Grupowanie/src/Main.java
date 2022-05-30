import java.io.*;
import java.util.*;

public class Main {
    static List<Data> dataList;
    static List<kData> kPointsList;
    static Map<kData, List<Data>> kDataListMap = new HashMap<>();
    static Map<Integer, Double> map;
    static int numOfK;
    static ArrayList<ArrayList<ArrayList<Double>>> tmpCluster;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please, print k: ");
        numOfK = Integer.parseInt(reader.readLine());
        boolean isItDone = false;
        tmpCluster = new ArrayList<>();
        map = new HashMap<>();
        kPointsList = new ArrayList<>();
        dataList = new ArrayList<>();
        fillingData();
        ConversionToK();
        for (int i = 0; i < kPointsList.size(); i++) {
            tmpCluster.add(new ArrayList<>(0));
        }
        int count = 0;
        while (!isItDone) {
            grouping();
            method();
            updateK();
            cleanser(kPointsList);
            grouping();
            isItDone = checkIfDifferent();
            tmpCluster.clear();
            for (int i = 0; i < kPointsList.size(); i++) {
                tmpCluster.add(new ArrayList<>(0));
            }
            updateK();
            cleanser(kPointsList);
            count++;
        }
        System.out.println("ENDS ON: "+count+" TIME");



    }
    public static boolean checkIfDifferent() {
        ArrayList<ArrayList<ArrayList<Double>>> list1 = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> list2 = new ArrayList<>();

        for (int i = 0; i < kPointsList.size(); i++) {
            list1.add(new ArrayList<>(0));
            list2.add(new ArrayList<>(0));
        }
        for (int i = 0; i <  kPointsList.size(); i++) {
            for (int j = 0; j < kPointsList.get(i).datas.size(); j++) {
                list1.get(i).add(kPointsList.get(i).datas.get(j).attributes);
            }
        }
        for (int i = 0; i < tmpCluster.size(); i++) {
            for (int j = 0; j < tmpCluster.get(i).size(); j++) {
                list2.get(i).add(tmpCluster.get(i).get(j));
            }
        }
        if (list1.equals(list2) == false) {
            System.out.println(list1);
            System.out.println();
            System.out.println(list2);
        }
        return list1.equals(list2);
    }

    public static void method() {
        for (int i = 0; i < kPointsList.size(); i++) {
            for (int j = 0; j < kPointsList.get(i).datas.size(); j++) {
                tmpCluster.get(i).add(kPointsList.get(i).datas.get(j).attributes);
            }
        }
    }

    public static void updateK() {
        for (int i = 0; i < kPointsList.size(); i++) {
            ArrayList<Double> list = new ArrayList<>();
            for (int j = 0; j < kPointsList.get(0).datas.get(0).attributes.size(); j++) {
                list.add((double) 0);
            }
            for (int j = 0; j < kPointsList.get(i).datas.size(); j++) {
                for (int k = 0; k < kPointsList.get(i).datas.get(j).attributes.size(); k++) {
                    list.set(k, list.get(k) + kPointsList.get(i).datas.get(j).attributes.get(k));
                }
            }
            for (int j = 0; j < kPointsList.get(i).points.size(); j++) {
                kPointsList.get(i).points.set(j, list.get(j) / kPointsList.get(i).datas.size());
                System.out.println(list.get(j) / kPointsList.get(i).datas.size());
            }
            list.clear();
        }
    }

    public static void fillingData() throws FileNotFoundException {
        File file = new File("C:\\Users\\Ян Региневич\\Desktop\\NAI\\GrupowanieV2\\DataSet");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String dataLine = scanner.nextLine();
            String[] parameters = dataLine.split(", ");
            ArrayList<Double> list = new ArrayList<>();
            for (int i = 0; i < parameters.length - 1; i++){
                list.add(Double.valueOf(parameters[i]));
            }
            dataList.add(new Data(list));
        }
        Collections.shuffle(dataList);
        scanner.close();
    }

    public static void printDataInformation() {
        System.out.println("+---------DATA--------+");
        for (Data data : dataList) {
            System.out.println("| " + data + " |");
        }
        System.out.println("+---------------------+");
    }

    public static void ConversionToK() {
        for (int i = 0; i < numOfK; i++) {
            ArrayList<Double> list = new ArrayList<>(dataList.get(i).attributes);
            kPointsList.add(new kData(list));
        }
    }

    public static void cleanser(List<kData> kPointsList) {
        for (int i = 0; i < kPointsList.size(); i++) {
            kPointsList.get(i).datas.clear();
        }
    }

    public static void grouping() {
        double count;
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j < kPointsList.size(); j++) {
                count = 0;
                for (int k = 0; k < dataList.get(j).attributes.size(); k++) {
                    count+=Math.pow(kPointsList.get(j).points.get(k) - dataList.get(i).attributes.get(k), 2);
                }
                map.put(j, count);
            }
            for (Map.Entry<Integer, Double> value : map.entrySet()) {
                if (value.getValue().equals(Collections.min(map.values()))) {
                    kPointsList.get(value.getKey()).datas.add(dataList.get(i));
                }
            }
        }

    }
    
}
