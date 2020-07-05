import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class CompletableFutureSimpleExample {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture completableFuture = CompletableFuture.runAsync(
                () -> System.out.println("Thread is running in "+Thread.currentThread()));

        System.out.println("In main thread"+ Thread.currentThread());

        // O/P when no thread pool is mentioned :
//        In main threadThread[main,5,main]
//        Thread is running in Thread[ForkJoinPool.commonPool-worker-9,5,main]

        ForkJoinPool pool = new ForkJoinPool(10);

        CompletableFuture completableFuture2 = CompletableFuture.runAsync(
                () -> System.out.println("Thread 2 is running in "+Thread.currentThread()),
                pool);

        System.out.println("In main thread"+ Thread.currentThread());
        //O/P :
//        In main threadThread[main,5,main]
//        Thread 2 is running in Thread[ForkJoinPool-1-worker-9,5,main]

        //Getting result from future

        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("called.."+Thread.currentThread());
            return 2;
        });
        Thread.sleep(1000); // this extra sleep helps to complete the task , so we might get result as 2 now.
        System.out.println(completableFuture3.getNow(-1));  // this means if the task 3 has completed, it will return 2 else we are using a default
        // value as -1

        // But using getNow also doesn't solve our need completely.


    }
}
