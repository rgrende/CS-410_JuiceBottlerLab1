// Rakiah L. Grende
// JuiceBottler, Lab 1
// Professor Nate Williams
// September 26th, 2022

//NOTES:
//worker class, will be separate workers
//two queues, producer and consumer queues.
//classes uppercase, first letter
//constance all capital w/ underscores
//methods or variables camelcase
//this.incoming  incoming queue
//outgoing queue
//runProcess

//Class Description:

//imports for Java

public class Worker implements Runnable { //this worker can be a thread, extending thread
    //is like a subtype of thread, instead running on separate thread.

    //class variables for the Worker class
    private final Thread thread;
    private Plant_MultiplePlant plant;
    private volatile boolean timeToWork;
    private Orange orange; //keeping track of what orange is held
    public String title;

    //constructor that passes in parameters number of threads and what plant the workers work for.
    public Worker(int threadNum, Plant_MultiplePlant plant) { //DELETED TITLE PARAM
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
            ridOf();
        } else {
            plant.obtainOrange(this);
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









