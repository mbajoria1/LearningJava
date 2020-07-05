import java.util.concurrent.*;

public class FutureWithException {

    public static Integer process() {
        System.out.println("Starting work..");
        if(sleep(5000))
            return 2;
        else
            return null;
        //throw new RuntimeException("Something went wrong!!");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Future<Integer> future = executorService.submit(() -> process());

        System.out.println(future.isDone());

        executorService.shutdown();

        System.out.println("In main thread: " + Thread.currentThread());

        //System.out.println("Value returned from future :" + future.get());
        // future.get() is a blocking call,until result is ready the line won't get printed, it will wait
        //sleep(5000);

        future.cancel(true);  // this will execute only if the future task hasn't completed yet. If above sleep command wasn't there,
        //then we would notice cancel has executed and it will print intrupted exception in log

        executorService.awaitTermination(6, TimeUnit.SECONDS);

        System.out.println(future.isDone());

        System.out.println(future.get()); // in case future thorws an exception, exception will be thrown in get method and any exception will be wrapped inside java.util.concurrent.ExecutionException object

        System.out.println("Terminated");
    }

    public static boolean sleep(int ms){
        try{
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException e) {

            System.out.println("Interrupted.. "+e);
            return false;
        }
    }
}
