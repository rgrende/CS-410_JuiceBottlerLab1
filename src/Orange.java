// Rakiah L. Grende
// JuiceBottler, Lab 1
// Professor Nate Williams
// September 26th, 2022

//Class Description: This is the orange class that describes the basic attributes of the oranges of the plant.
// In this class, the oranges state is determined by the method getState() and the process to change the orange's state is also found here.
// This class was also provided by Professor Williams and was not altered.


public class Orange {
    public enum State {
        Fetched(15),
        Peeled(38),
        Squeezed(29),
        Bottled(17),
        Processed(1);

        private static final int finalIndex = State.values().length - 1;

        final int timeToComplete;

        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state.");
            }
            return State.values()[currIndex + 1];
        }
    }

    //sets the state of the orange
    private State state;

    //Method to establish an orange, the concept of an orange
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    //returns the current state of the orange, so the worker knows what state it needs to be next
    public State getState() {
        return state;
    }

    //Method that runs the process to change the oranges state
    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed.");
        }
        doWork();
        state = state.getNext();
    }

    //Method that performs work on the orange to change state
    private void doWork() {
        // Sleep for the amount of time necessary to do the work
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad.");
        }
    }
}
