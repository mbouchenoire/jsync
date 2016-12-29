import java.util.UUID;

/**
 * @author mbouchenoire
 */
class Counter {

    private final UUID uuid;

    private int count;

    public Counter() {
        this(0);
    }

    public Counter(int start) {
        super();
        this.uuid = UUID.randomUUID();
        this.count = start;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Counter counter = (Counter) o;

        return uuid != null ? uuid.equals(counter.uuid) : counter.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "count=" + count +
                '}';
    }
}
