# Jsync

## Basic usage

```java
import com.mbouchenoire.jsync.Jsync;

public class App {
    
    public static void main(String[] args) {
        
        Jsync.parallel(
            new Runnable() {
                public void run() {
                    // Do some long task here...
                }
            },
            new Runnable() {
                public void run() {
                    // Do some other long task there...
                }
            },
            new CustomRunnable(args) // or implement your own runnable
        );
        
        String[] strings = new String[] { "hello" , "world" };
        
        Integer[] lengths = Jsync.map(strings, new Function<String, Integer>() {
           public Integer apply(String arg) {
               // each execution of this function is asynchronous
               return arg.length();
           }
        });
        
    }
}
```



