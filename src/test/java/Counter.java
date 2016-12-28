/**
 * @author mbouchenoire
 */
class Counter {

    private int count;

    public Counter(int start) {
        super();
        this.count = start;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count += 1;
    }
}
