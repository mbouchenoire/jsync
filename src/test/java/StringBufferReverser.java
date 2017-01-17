/**
 * @author mbouchenoire
 */
public class StringBufferReverser implements Runnable{

    private StringBuffer stringBuffer;

    public StringBufferReverser(StringBuffer stringBuffer) {
        if (stringBuffer == null)
            throw new IllegalArgumentException("stringBuffer");

        this.stringBuffer = stringBuffer;
    }

    public void run() {
        this.stringBuffer.reverse();
    }
}
