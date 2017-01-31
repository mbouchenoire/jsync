# <img src="logo.png" alt="Drawing" style="width: 20px;"/> Jsync 
[![Build Status](https://travis-ci.org/mbouchenoire/jsync.svg?branch=master)](https://travis-ci.org/mbouchenoire/jsync)&nbsp;
[![Coverage Status](https://coveralls.io/repos/github/mbouchenoire/jsync/badge.svg?branch=master)](https://coveralls.io/github/mbouchenoire/jsync?branch=master)

**Jsync** is a lightweight Java library focused on providing simple methods to deal with concurrency.
This library is mostly influenced by the [.NET Parallel Class](https://msdn.microsoft.com/en-us/library/system.threading.tasks.parallel(v=vs.110).aspx) and [async.js](https://github.com/caolan/async). 

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

### `forEach(T[] items, Consumer<T> consumer)`
Apply each value in `items` to the [`Consumer`](src/main/java/com/mbouchenoire/jsync/Consumer.java).
Each execution of [`Consumer#accept()`](src/main/java/com/mbouchenoire/jsync/Consumer.java) is called asynchronously while the `forEach()` method itself is synchronous.

```java
String[] strings = new String[] { "hi", "jsync" };

Jsync.forEach(strings, new Consumer<String>() {
    public void accept(String arg) {
        // each execution of this function is asynchronous
        System.out.println(arg);
    }
});
```

### `T[] map(T[] items, Function<T, R> function)`
Produces a new `Array` / `Collection` of values by mapping each value in `items` through the [`Function`](src/main/java/com/mbouchenoire/jsync/Function.java).
Each execution of [`Function#apply()`](src/main/java/com/mbouchenoire/jsync/Function.java) is called asynchronously while the `map()` function itself is synchronous.

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

### `T[] filter(T[] items, Predicate<T> predicate)`
Produces a new `Array` / `Collection` of values which pass the [`Predicate`](src/main/java/com/mbouchenoire/jsync/Predicate.java) test.
Each execution of [`Predicate#test()`](src/main/java/com/mbouchenoire/jsync/Predicate.java) is called asynchronously while the `filter()` function itself is synchronous.

```java
String[] strings = new String[] { "hi", "jsync", "this is too long" };

String[] filteredStrings = Jsync.filter(strings, new Predicate<String>() {
    public Boolean test(String arg) {
        // each execution of this function is asynchronous
        return (arg.length <= 10);
    }
});

// filteredStrings : [ "hi", "jsync" ]
```



