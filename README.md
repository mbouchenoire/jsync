# Jsync

## Examples

### parallel
```java
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
    new CustomRunnable(args) // or implement your own runnables
);
```

### map
```java
String[] strings = new String[] { "hi" , "Jsync" };

Integer[] lengths = Jsync.map(strings, new Function<String, Integer>() {
    public Integer apply(String arg) {
        // each execution of this function is asynchronous
        return arg.length();
    }
});

// lengths = [2, 5]
```


