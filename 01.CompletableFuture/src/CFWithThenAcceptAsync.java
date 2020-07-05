import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class CFWithThenAcceptAsync {

    public static int process() {
        sleep(1000);
        System.out.println("Called:" + Thread.currentThread());
        return 2;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(10);
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> process(),pool);
        sleep(1000);
        System.out.println("In main thread:" + Thread.currentThread());
        //sleep(2000);
        ForkJoinPool pool2 = new ForkJoinPool(10);
        result.thenAcceptAsync(CFWithThenAccept::print,pool2);
        System.out.println("Done");
        //sleep(3000);
    }

    public static void print(int value) {
        System.out.println(value + ".." + Thread.currentThread());
    }

    public static boolean sleep(int ms) {
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException e) {

            System.out.println("Interrupted.. " + e);
            return false;
        }
    }
}
