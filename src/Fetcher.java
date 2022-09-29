//class for the fetcher position that extends the worker class.
public class Fetcher extends Worker {
    public Fetcher(int threadNum, Plant_MultiplePlant plant) {
        super(threadNum, plant);
        title = "Fetcher";
    }
}
