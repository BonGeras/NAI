import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Perceptron {
    ArrayList<Double> wagi;
    double alpha;
    double t;
    String language;

    Perceptron(String language) {
        wagi = new ArrayList<>();
        alpha = 0.7;
        double count = 0;
        t = ThreadLocalRandom.current().nextDouble(0, 0.99 + 0.01);
        for (int i = 0; i < 26; i++) {
            wagi.add(Math.random());
        }

        for (Double aDouble : wagi) {
            count += Math.pow(aDouble, 2);
        }
        count=Math.sqrt(count);
        for (int i = 0; i < wagi.size(); i++) {
            wagi.set(i,wagi.get(i)/count);
        }
        this.language = language;

    }
    public ArrayList<Double> getWagi() {
        return wagi;
    }
    public double getT() {
        return t;
    }
    public String getLanguage() {
        return language;
    }
}
