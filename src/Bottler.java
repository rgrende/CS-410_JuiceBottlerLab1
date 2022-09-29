//Class for the bottler position that extends the worker class.
public class Bottler extends Worker {
    public Bottler(int threadNum, Plant plant) {
        super(threadNum, plant);
        title = "Bottler";
    }
}
