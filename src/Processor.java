//Class for the processor position that extends the worker class.
public class Processor extends Worker {
    public Processor(int threadNum, Plant plant) {
        super(threadNum, plant);
        title = "Processor";
    }
}
