# Future & Completable Future

## Java Future

Future was introduces in Java 5 timeframe. It was used for asynchronous programming.But it had few issues which is the reason for introducing Completable Future in Java8. Lets discuss few problems of Future and how newer libary resolved those.
<br/>
**The good old ExecutorService** was used to create a pool of threads and use it for parallel programming. We can create a thread by using Thread class but thats not very effient way to handle multiple threads. Below program shows a simple code running two paralled threads. One main thread and one thread pool having 100 threads.
<br/>

> For full program, refer to src folder
> **Invoking a task & performing an action**

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

If we keep main thread name print & `result.get()` in two different lines, we can see main thread will be printed before future task is complete.Below code & o/p
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
<br/>

### Java Future exception

When there is an exception in execution of a concurrent process, the exception is wrapped inside ExecutionException object.
`future.isDone()` - returns true when future task is completed or it blows up(throws an exception) or if the future has been cancelled.
`future.cancel(true)` - is used to cancel a task which hasn't yet completed. If a future task has already completed, executing `cancel()` will not do anything
as there is nothing to cancel, it will simply be ignored. `future.cancel(false)` doesn't do anything.

### Future of Java Future

Future has lot of limitations.

1. Getting result using future.get() is a blocking call.
2. When scheduling multiple tasks if one of the task fails all future task's state is not known clearly. Exception/error handling
   can't be done efficiently in Future.
   <br/>

## Completable Future:

1. CF is nothing but promises in Javascript.
2. It has two channel. One for data & another for error/exceptions.
3. CF is pipeline of stages. When one stage completes , next one takes over. If any stage has any exception, then flow comes to
   exception channel and then its upto us how we want to handle it. We can send the control back to data channel or handle that in exception channel
   itself.

### Creating a CF

```
CompletableFuture completableFuture = CompletableFuture.runAsync(
                () -> System.out.println("Thread is running in "+Thread.currentThread()));
```

#### What thread it is running in

- if no thread pool is specified then all async thread are run in forkjoin common pool
  <br/>

### Creating pool of threads to run async threads

```
ForkJoinPool pool = new ForkJoinPool(10);

CompletableFuture completableFuture2 = CompletableFuture.runAsync(
                () -> System.out.println("Thread 2 is running in "+Thread.currentThread()),
                pool);
```

### Running a task that yields result

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
```

### Get & getNow

[CompletableFutureSimpleExample.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableFutureSimpleExample.java)
<br/>

- `get` is a blocking call where as `getNow` provides an option to provide a default value in case the future task hasn't completed yet.

`System.out.println(completableFuture3.getNow(-1)); // if future returns an integer than default value is -1.`

## Completable thenAccept

[CFWithThenAccept.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CFWithThenAccept.java)
<br/>
<br/>
Accept method is a consumer which takes a value as input but doesn't return anything. It just does some processing
with it. With Completable, we can use thenAccept to pass a consumer to perform same kind of operation.

> Note: For CF, when the future task has completed and we want to get the result
> out of it, which thread, the thread executing the future task or the main thread which creates
> the future task will do the work depends upon if main thread is busy in other task or not. **Program CFWithThenAccept** explains it. Verify and play with it to understand different outputs in different scenarios.

## Completable async methods

[CFWithThenAcceptAsync.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CFWithThenAcceptAsync.java)
<br/>
<br/>
| Future task status | Non-async method like thenAccept | Async method like thenAcceptAsync |
| ------------------- | ---------------------------------------------- | --------------------------------- |
| CF has completed | The caller thread/main will execute | It will run in another thread |
| CF hasn't completed | The thread completing future task will execute | It will run in another thread |

> Note: For async only thing to keep in mind is, if we provide a pool, it will execute in threads from that pool, but if we don't provide a pool, then it will execute in common pool.

## Completable thenApply

[CFWithThenApply.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CFWithThenApply.java)
<br/>
<br/>
thenApply can be used to call a function(A method which takes one or multiple parameters and returns a value as well)

```
    public static int transform(int value){
        System.out.println("Transform called in thread:"+Thread.currentThread());
        return value *2;
    }

        CompletableFuture
                .supplyAsync(CFWithThenApply::process)   // returns an integer value
                .thenApply(CFWithThenApply::transform)   // Takes the integer and return double of it.
                .thenAccept(System.out::println);
```

## Completable thenRun

[CFWithThenRun.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CFWithThenRun.java)
<br/>
<br/>
thenRun can be used in different use cases such as at the end of an process execution for
logging purposes or to print a message in console etc. Below is a simple working example.

```
CompletableFuture
                .supplyAsync(CFWithThenApply::process)
                .thenApplyAsync(CFWithThenApply::transform)
                .thenAcceptAsync(CFWithThenApply::print)
                .thenRun(() -> System.out.println("We are done here..."));

```

## Completable complete

[CompletableComplete.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableComplete.java)
<br/>
<br/>
Instead of calling a supply/apply/accept on a future, we can instead first build a pipeline of stages/tasks that needs to be completed.
Then pass the future to a separate method to perform the task. In this case, when we pass just the future it wont perform the tasks until
we execute `CompletableFuture.complete()` operation on it. `java CompletableComplete` code has example of the same. Please verify it.
CompletableFuture has multiple stages. When we build the pipleline, CF will be in "Not Completed, 1 dependent" stage as it has a pipeline which needs to be completed. Then after the future task
has been completed it's stage will be "completed normally" stage.

## Completable cancelling

[CompletableCancel.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableCancel.java)
<br/>
<br/>
State of a completableFuture will be in not completed until the future task has finished execution. We can verify status details of a CF
by calling `isDone()` method instead of printing CF object as done in previous example. `isDone()` returns true when future has completed
execution or it has ended abruptly due to an exception or future has been cancelled.

`completableFuture.cancel(true)` - cancels the future only if it hasn't completed yet. Similarly when we call `completableFuture.complete()`
will be executed only if future hasn't completed yet or if it hasn't been cancelled yet.
If future has already been cancelled then `complete()` wouldn't do anything , it will simply be ignored.

## Completable Exceptions

[CompletableExceptions.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableExceptions.java)

`exceptionally()` - this supports to handle exceptions caused while executing a future pipeline. If there are any exceptions then you can either continue the workflow in error channel and exit the flow by throwing a proper error message or you may pass the control to again data channel. How you want to handle it, its upto the requirement you have.

`completableFuture.completeExceptionally(new RuntimeException("The sky is pink"));` <br/> - you can complete a future task with an intensional exception in case of different error scenarions rather than completing normally.

`completableFuture.isCompletedExceptionally()` will be true when tasks completes with an exception.<br/>
`completableFuture.isCompleted()` - true when task completes normally.
<br/>

## Completable chaining

[CompletableChaining.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableChaining.java)

Completable in java can be though of as two tracks.

1. Complete/Data track.
2. Exception track.

If CF fails or blows up at any stage then control shifts from data track to exception track/channel.
From exception channel it can come back to data channel based on how we handle the exception in our code or it can continue to stay on exception channel. Based on this, there are 4 different scenarios which can happen.

1. Complete to complete
2. Complete to exception
3. Exception to complete
4. Exception to Exception

## Completable thencombine

[CompletableThenCombine.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableThenCombine.java)
<br/>

`thenCombine` can be used to combine the result of two `completableFuture` . In below code example, google & amzn
contains stock prices for their companies. thenCombine is used to sum up the price to get total amount.

```
CompletableFuture<Double> google = getFuture("google",200);
        CompletableFuture<Double> amzn = getFuture("amazon",300);

google.thenCombine(amzn , Double::sum)
                .thenAccept(CompletableThenCombine::print);

```

## Completable thencompose

[CompletableThenCompose.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableThenCompose.java)

If a method takes a value but returns a stream or future object then instead of `thenApply` , we need to use `thenCompose` which is used to extract the result from future object.

```
public static void main(String[] args) {
        getStockName(1)
        .thenCompose(stock -> getFuture(stock,200))
        // as getFuture takes values but returns CF.
        .thenAccept(System.out::println);
}

public static CompletableFuture<Double> getFuture(String stockName, int noOfShares){
        return CompletableFuture.supplyAsync(() -> getPrice(stockName) * noOfShares);
}
```

## Completable `acceptEither`/`applyEither`

[CFAcceptApplyEither.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CFAcceptApplyEither.java)
<br/>
<br/>
acceptEither accepts result from any completableFuture which completes faster. So if we have two CFs, acceptEither will take 1st one to complete and perform an action. For exception also, if 1st one to complete throws an exception then it will complete exceptionally else it will complete successfully.

## Completable runAfter

[CompletableRunAfter.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableRunAfter.java)
<br/>
<br/>

## Completable anyof

[CompletableAnyof.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableAnyof.java)

CF `anyof` can take multiple `completableFuture` as input and returns the one which completes first. Anyof can take different type of CFs. See below example. Because of accepting different types, the returned CF is an object. So, without knowing object type you cannot apply direct opeartions on it. You need to check its type first.

```
  CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> generate(1));
  CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> generate(2));
  CompletableFuture<Integer> c3 = CompletableFuture.supplyAsync(() -> generate(3));
  CompletableFuture<Double>  c4 = CompletableFuture.supplyAsync(() -> generate());
  CompletableFuture<String>  c5 = CompletableFuture.supplyAsync(() -> generate("Madhuri"));

    CompletableFuture.anyOf(c1, c2, c3, c4, c5)
           .thenAccept(System.out::println);
```

In case of anyOf, which ever CF completes first the final o/p depends on that.

- In case the 1st one completes successfully then anyOf CF completes normally.
- If 1st one thorws an exception then CF completes exceptionally.
- If 1st one is cancelled, then it CF cancells.
  <br/>
  <br/>

## Completable timeout

[CompletableTimeout.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableTimeout.java)

For `anyOf` function we can introduce a CF with a timeout value. If none of the CFs responds within that SLA, `anyOf completableFuture` will complete exceptionally thworing a timeout exception.

```
    public static CompletableFuture<Void> createTimeout(int ms){
        return CompletableFuture.supplyAsync(() ->{
            sleep(ms);
            throw new RuntimeException("Timeout reached");
        });
    }

    CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> generate(1));
    CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> generate(2));
    CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> generate(3));
    CompletableFuture<Integer> cf4 = CompletableFuture.supplyAsync(() -> generate(4));
    CompletableFuture<Integer> cf5 = CompletableFuture.supplyAsync(() -> generate(5));
    CompletableFuture<Void> cf6 = createTimeout(3000);

    // O/P for timeout: Error? java.lang.RuntimeException: Timeout reached
    CompletableFuture.anyOf(cf1, cf2, cf3, cf4, cf5,cf6)
        .exceptionally(CompletableTimeout::report)
        .thenApply(value -> (int) value * 10)
        .thenAccept(System.out::println);
```

<br/>
## Completable allOf

[CompletableAllof.java](https://github.com/mbajoria1/LearningJava/blob/master/01.CompletableFuture/src/CompletableAllof.java)

In case of `anyOf` the returned CF is of type object but for `allOf` the same is of type void.

```
 CompletableFuture<Object> c1 = CompletableFuture.anyOf(cf1, cf2, cf3, cf4, cf5,cf6);
 CompletableFuture<Void> c1 = CompletableFuture.allOf(cf1, cf2, cf3, cf4, cf5,cf6);
```

For allOf completableFuture will not complete unless all of the CFs completes.
If one of them thorws exception, then also it will wait until all of them has executed. If one of then is cancelled , then the CF will also be thowing cancel exception.

<br/>
<br/>

## Completable using allOf

CompletableAllof.java has been modified to explain some of the uses of `allOf`.
