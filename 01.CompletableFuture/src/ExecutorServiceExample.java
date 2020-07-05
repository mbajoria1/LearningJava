import java.util.concurrent.*;

public class ExecutorServiceExample {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        executorService.submit(() -> System.out.println("Task is running in thread," + Thread.currentThread()));

        executorService.shutdown();  // invoking shutdown on executorService doesn't terminate all the threads immediately
        // as there might be some task still performing some tasks. So it may take a while to terminate

        System.out.println("Awaiting termination of the task for thread,"+ Thread.currentThread());

        executorService.awaitTermination(30000,TimeUnit.SECONDS);

        System.out.println("Terminated");

        // Above program, might print line Awaiting & task running in sout randomly as both are executing in parallel.
        //Below is the program output but the o/p may vary at times. Task running line might be printed first than the 2nd line sometimes.
        //Awaiting termination of the task for thread,Thread[main,5,main]
        //Task is running in thread,Thread[pool-1-thread-1,5,main]
        //Terminated
    }
}
