public class Squeezer extends Worker {
    public Squeezer(int threadNum, Plant_MultiplePlant plant) {
        super(threadNum, plant);
        title = "Squeezer";
    }
}
