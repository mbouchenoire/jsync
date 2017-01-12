/**
 * @author mbouchenoire
 */
public class StringBuilderReverser implements Runnable{

    private StringBuilder stringBuilder;

    public StringBuilderReverser(StringBuilder stringBuilder) {
        if (stringBuilder == null)
            throw new IllegalArgumentException("stringBuilder");

        this.stringBuilder = stringBuilder;
    }

    public void run() {
        this.stringBuilder.reverse();
    }
}
