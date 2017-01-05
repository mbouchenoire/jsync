# Jsync

## Basic usage

```java
import com.mbouchenoire.jsync.Jsync;

public class App {
    
    public static void main(String[] args) {
        
        Jsync.parallel(
            new Runnable() {
                @Override
                public void run() {
                    // Do some long task here...
                }
            },
            new Runnable() {
                @Override
                public void run() {
                    // Do some other long task there...
                }
            },
            new CustomRunnable(args) // or implement your own runnable
        );
        
    }
}
```



