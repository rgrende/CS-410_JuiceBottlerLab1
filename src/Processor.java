public class Processor extends Worker {
    public Processor(int threadNum, Plant_MultiplePlant plant) {
        super(threadNum, plant);
        title = "Processor";
    }
}
