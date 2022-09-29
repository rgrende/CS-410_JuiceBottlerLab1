// Rakiah L. Grende
// JuiceBottler, Lab 1
// Professor Nate Williams
// September 26th, 2022


//Class Description: This is the worker class. Here the basic outline of a worker is defined with qualities
// that will be re-described in each of the worker type classes, ie. fetcher, peeler, etc. This class is responsible
// for doing the work in the plant. If a worker does not have an orange to work on while timeToWork() is set to true, then
// that worker will either obtain an orange if there is one already in the queue or if it is the fetcher, it will spawn a
// new orange to work on. Once the worker has worked on the orange, they release it back to the appropriate queue and
// give it back to the plant. Each position is established in its own class but extend the worker class.

public class Worker implements Runnable { //this worker can be a thread, extending thread
    //is like a subtype of thread, instead running on separate thread.

    //class variables for the Worker class
    private final Thread thread;
    private Plant plant;
    private volatile boolean timeToWork;
    private Orange orange; //keeping track of what orange is held
    public String title;

    //constructor that passes in parameters number of threads and what plant the workers work for.
    public Worker(int threadNum, Plant plant) { //DELETED TITLE PARAM
        thread = new Thread(this, "Plant[" + threadNum + "]");
        this.plant = plant;
        this.title = null;
        this.timeToWork = false;
    }

    //implement own do work class
    //basic worker methods
    public void startWorker() {
        timeToWork = true;
        thread.start();
    }

    //method to stop worker
    public void stopWorker() {
        timeToWork = false;
    }

    //wait to stop the workers from working
    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    //assigns a title, such as fetcher, for the workers at the start, returns a title
    public String getTitle() {
        return title;
    }

    //tells the worker to do work on the orange and then release the orange back to the plant.
    protected void doWork() {
        if (orange != null) {
            orange.runProcess();
            plant.chuckOrange(orange);
            try {
                ridOf();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            orange = plant.obtainOrange(this);
        }
    }

    //checks to see if the worker has an orange, if not, throws exception
    //if there is an orange, set it back to null as if the worker gets rid of the orange
    public void ridOf() {
        if (orange == null) {
           throw new NullPointerException("No orange to release because it did not exist.");
        }
        orange = null;
    }

    //run method for worker class
    //while the plant is open and its working time, the workers will do their job.
    public void run() {
        while  (timeToWork) {
            doWork();
            }
        }
    }









