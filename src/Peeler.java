public class Peeler extends Worker {
    public Peeler(int threadNum, Plant_MultiplePlant plant) {
        super(threadNum, plant);
        title = "Peeler";
    }
}
