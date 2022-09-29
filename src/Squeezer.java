//Class for the squeezer position that extends the worker class.
public class Squeezer extends Worker {
    public Squeezer(int threadNum, Plant plant) {
        super(threadNum, plant);
        title = "Squeezer";
    }
}
