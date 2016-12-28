/**
 * @author mbouchenoire
 */
class CounterIncremeterRunnable implements Runnable {

    private final Counter counter;

    public CounterIncremeterRunnable(Counter counter) {
        this.counter = counter;
    }


    public void run() {
        this.counter.increment();
    }
}
