import java.util.concurrent.*;

public class GettingResultFromExecutorService {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
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

        executorService.shutdown();  // invoking shutdown on executorService doesn't terminate all the threads immediately
        // as there might be some task still performing some tasks. So it may take a while to terminate

        System.out.println("In main thread: " + Thread.currentThread());
        System.out.println("Value returned from future :" + future.get());
        // future.get() is a blocking call,until result is ready the line won't get printed, it will wait

        executorService.awaitTermination(30000, TimeUnit.SECONDS);

        System.out.println("Terminated");

    }
}
