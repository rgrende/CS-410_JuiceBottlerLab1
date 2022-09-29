// Rakiah L. Grende
// JuiceBottler, Lab 1
// Professor Nate Williams
// September 26th, 2022


//Class Description: This is the plant class, which runs multiple plants at once. This code was provided by
// Professor Nate Williams. The plant has four shared queues between five workers. The worker runs a process on the
// orange that they have and then returns it back to the plant. The plant keeps track of oranges provided to the workers,
// oranges processed by the plant, how many bottles were produced, and finally how much was wasted. The program starts out
// by setting timeToWork() to true and then notifies the workers to start working. The works are assigned a title upon the
// program start up.

//Imports for Java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public static final long PROCESSING_TIME = 1 * 1000;

    private static final int NUM_PLANTS = 2;
    private static final int WORKER_GROUPS_PER_PLANT = 2;

    public static void main(String[] args) {
        // Startup the plants
        Plant[] plantMultiplePlants = new Plant[NUM_PLANTS];
        for (int i = 0; i < NUM_PLANTS; i++) {
            plantMultiplePlants[i] = new Plant(i); //is this supposed to be i?
            plantMultiplePlants[i].startPlant();
        }

        // Give the plants time to do work
        delay(PROCESSING_TIME, "Plant malfunction");

        // Stop the plant, and wait for it to shut down
        for (Plant p : plantMultiplePlants) {
            for (Worker worker: p.worker)
                worker.stopWorker();
            p.stopPlant();
        }
        for (Plant p : plantMultiplePlants) {
            for (Worker worker: p.worker)
                worker.waitToStop();
            p.waitToStop();
        }

        // Summarize the results
        int totalProvided = 0;
        int totalProcessed = 0;
        int totalBottles = 0;
        int totalWasted = 0;
        for (Plant p : plantMultiplePlants) {
            totalProvided += p.getProvidedOranges();
            totalProcessed += p.getProcessedOranges();
            totalBottles += p.getBottles();
            totalWasted += p.getWaste();
        }
        System.out.println("Total provided/processed = " + totalProvided + "/" + totalProcessed);
        System.out.println("Created " + totalBottles +
                           ", wasted " + totalWasted + " oranges");
    }

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    //ensure this variable can be altered and program still runs.
    public final int ORANGES_PER_BOTTLE = 3;

    private BlockingQueue<Orange> peelerQueue;
    private BlockingQueue<Orange> squeezerQueue;
    private BlockingQueue<Orange> bottlerQueue;
    private BlockingQueue<Orange> processingQueue;

    private Worker[] worker;

    private final Thread thread;
    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;

    Plant(int threadNum) { //constructor
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, "Plant[" + threadNum + "]");

        peelerQueue = new LinkedBlockingQueue<>();
        squeezerQueue = new LinkedBlockingQueue<>();
        bottlerQueue = new LinkedBlockingQueue<>();
        processingQueue = new LinkedBlockingQueue<>();

        //Implementing a Linked Blocking List
        worker = new Worker[5*WORKER_GROUPS_PER_PLANT]; //initializing "tables" for workers

        for (int i = 0; i < 5*WORKER_GROUPS_PER_PLANT; i = i + 5) {
            worker[i] = new Fetcher(0, this);
            worker[i+1] = new Peeler(1, this);
            worker[i+2] = new Squeezer(2, this);
            worker[i+3] = new Bottler(3, this);
            worker[i+4] = new Processor(4, this);
        }
        for(Worker worker: worker) {
            worker.startWorker();
        }
    }

    //method that starts the plant
    public void startPlant() {
        timeToWork = true;
        thread.start();
    }

    //method that stops the plant
    public void stopPlant() {
        timeToWork = false;
        //stop the workers
    }

    //method that waits to stop the plant
    public void waitToStop() {
        try {
            thread.join();
            //wait for the workers to stop working
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    //run method for Plant_MultiplePlant
    //while the there is time to work, the plant is open and the workers are doing their job
    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges ");
        while (timeToWork) {
            continue;
            //plant is open, workers are working...
        }
        System.out.println("");
        System.out.println(Thread.currentThread().getName() + " Done");
    }

    //acquire an orange from the plant
    public synchronized Orange obtainOrange(Worker worker) {
        String title = worker.getTitle();
        //switch statement to assign a title to a worker
        switch (title) {
            case "Fetcher":
                orangesProvided++;
                System.out.println("Fetcher got an orange.");
                return new Orange();
            case "Peeler":
                if (!peelerQueue.isEmpty()) {
                    System.out.println("Peeler peeled an orange.");
                    return peelerQueue.remove();
                }
                try {
                    worker.wait();
                } catch (Exception e) {}
                break;
            case "Squeezer":
                if (!squeezerQueue.isEmpty()) {
                    System.out.println("Squeezer squeezed an orange.");
                    return squeezerQueue.remove();
                }
                try {
                    worker.wait();
                } catch (Exception e) {/*Ignored*/}
                break;
            case "Bottler":
                if (!bottlerQueue.isEmpty()) {
                    System.out.println("Bottler bottled an orange.");
                    return bottlerQueue.remove();
                }
                try {
                    worker.wait();
                } catch (Exception e) {}
                break;
            case "Processor":
                if (!processingQueue.isEmpty()) {
                    System.out.println("Processor processed an orange.");
                    return processingQueue.remove();
                }
                try {
                    worker.wait();
                } catch (Exception e) {}
                break;
        }
        return null;

    }

    //release the orange to the plant
    public synchronized void chuckOrange(Orange orange) {
        if (orange == null) {
            throw new NullPointerException("No orange to release because it did not exist.");
        }
        //switch statement to determine the state of the orange
        switch (orange.getState()) {
            case Peeled:
                peelerQueue.add(orange);
                break;
            case Squeezed:
                squeezerQueue.add(orange);
                break;
            case Bottled:
                bottlerQueue.add(orange);
                break;
            case Processed:
                orangesProcessed++;
                break;
        }
        //after worker changes orange state and releases back to plant,
        // all the workers and the plant needs to be notified in order to continue the process
        notifyAll();
    }

    //method that gets the oranges provided by the plant
    //returns the number of oranges provided
    public int getProvidedOranges() {
        return orangesProvided;
    }

    //method that gets the oranges processed
    //returns the number of oranges processed by the plant
    public int getProcessedOranges() {
        return orangesProcessed;
    }

    //method that gets the number of bottles produced by the plant
    //returns the number of bottles processed by dividing the number of oranges provided
    // by oranges per bottle
    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    //method to determine how many oranges are wasted.
    //returns the number of oranges wasted by dividing the number of oranges processed by oranges per bottle
    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}
