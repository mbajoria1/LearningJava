# Future & Completable Future

## Java Future

Future was introduces in Java 5 timeframe. It was used for asynchronous programming.But it had few issues which is the reason for introducing Completable Future in Java8. Lets discuss few problems of Future and how newer libary resolved those.

**The good old ExecutorService** was used to create a pool of threads and use it for parallel programming. We can create a thread by using Thread class but thats not very effient way to handle multiple threads. Below program shows a simple code running two paralled threads. One main thread and one thread pool having 100 threads.

> For full program, refer to src folder
**Invoking a task & performing an action**

```
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.submit(() -> System.out.println("Task is running in thread," + Thread.currentThread()));

        executorService.shutdown();
        // invoking shutdown on executorService doesn't terminate all the threads immediately as there might be some threads still performing some tasks. So it may take a while to terminate

        executorService.awaitTermination(30000,TimeUnit.SECONDS);  //Awaiting termination provides extra time for threads to terminate
        System.out.println("Terminated");
```

**Getting result**

For getting result from executorService we need to use Callable as Runnable doesn't return any data. Getting data from a future object and blocking nature of it.

```
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Starting work");
                Thread.sleep(1000);
                System.out.println("Task is getting executed in,"+Thread.currentThread());
                return 2;
            }
        });

        System.out.println("In main thread: " + Thread.currentThread() + " Value returned from future :" + future.get());
        // future.get() is a blocking call,until result is ready the line won't get printed, it will wait

```
If we keep main thread name print & result.get() in two different lines, we can see main thread will be printed before future task is complete.Below code & o/p
explains that.

```
System.out.println("In main thread: " + Thread.currentThread());
System.out.println("Value returned from future :" + future.get());
```

Then Output is as below:
Starting work
**_In main thread: Thread[main,5,main]_** - this line is printed before and when result is ready in future then 2 is printed.
Task is getting executed in,Thread[pool-1-thread-1,5,main]
Value returned from future :2

## Java Future exception

When there is an exception in execution of a concurrent process, the exception is wrapped inside ExecutionException object.
future.isDone() - returns true when future task is complete.
future.cancel(true) - is used to cancel a task which hasn't yet completed. If a future task has already completed, executing cancel() will not do anything
as there is nothing to cancel.

## Future of Java Future


