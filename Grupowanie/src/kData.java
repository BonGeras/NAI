import java.util.ArrayList;

public class kData {
    ArrayList<Double> points;
    ArrayList<Data> datas;
    public kData (ArrayList<Double> points) {
        this.points = points;
        datas = new ArrayList<>();
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public void setData(Data data) {
        datas.add(data);
    }

    public ArrayList<Double> getPoints() {
        return points;
    }
}

