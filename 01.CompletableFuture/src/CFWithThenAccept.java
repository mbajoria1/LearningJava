import java.util.concurrent.CompletableFuture;

public class CFWithThenAccept {
    public static int process(){
        sleep(1000);
        System.out.println("Called:"+Thread.currentThread());
        return 2;
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> process());
        sleep(1000);
        System.out.println("In main thread:"+Thread.currentThread());
        //sleep(2000);
        result.thenAccept(CFWithThenAccept::print);
        System.out.println("Done");
        //sleep(3000);

        // Here in above code, when thenAccept is called, future task hasn't completed as process method has more
        // sleep time(2 Sec) than main(1 sec). When the task completes after 2 sec, main thread is busy in another
        // sleep of 3 seconds. So, the thread which finishes the task completes the work of assigned in thenAccept method.
        // If main wasn't busy in sleep, main would have done the work.
        //O/P in sleep 3000 being there in code:
      /*  In main thread:Thread[main,5,main]
        Done
        Called:Thread[ForkJoinPool.commonPool-worker-9,5,main]
        2..Thread[ForkJoinPool.commonPool-worker-9,5,main]*/

      // O/P when sleep(3000) is commented sleep of process method is less , so that it
        // completes its work before thenAccept is called:
//        In main thread:Thread[main,5,main]
//        Called:Thread[ForkJoinPool.commonPool-worker-9,5,main]
//        2..Thread[main,5,main]
//        Done
        // Conclusion: task assigned in thenAccept would be executed by main thread if the CF task has already
        // completed else, it may be done by the thread which executes the future task.


    }

    public static void print(int value){
        System.out.println(value+".."+Thread.currentThread());
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
