//Class for the peeler position that extends the worker class.
public class Peeler extends Worker {
    public Peeler(int threadNum, Plant plant) {
        super(threadNum, plant);
        title = "Peeler";
    }
}
