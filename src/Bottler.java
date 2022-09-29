public class Bottler extends Worker {
    public Bottler(int threadNum, Plant_MultiplePlant plant) {
        super(threadNum, plant);
        title = "Bottler";
    }
}
