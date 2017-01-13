# Jsync

## Methods

### `parallel(Runnable[] runnables)`
Call each given [`Runnable`](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Runnable.html) asynchronously while beeing synchronous itself.
You can pass as many runnables as you want using [`varargs`](http://docs.oracle.com/javase/1.5.0/docs/guide/language/varargs.html), or even provide them within an `Array` / `Collection`.

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

### `T[] map(T[] items, Function<T, R> function)`
Produces a new `Array` / `Collection` of values by mapping each value in `items` through the `function`.
Each execution of the `function` is called asynchronously while the `map()` function itself is synchronous.

```java
String[] strings = new String[] { "hi" , "Jsync" };

Integer[] lengths = Jsync.map(strings, new Function<String, Integer>() {
    public Integer apply(String arg) {
        // each execution of this function is asynchronous
        return arg.length();
    }
});

// lengths : [2, 5]
```


