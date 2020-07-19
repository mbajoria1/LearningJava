import java.util.concurrent.CompletableFuture;

public class CompletableTimeout {

    public static boolean sleep(int ms) {
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException e) {

            System.out.println("Interrupted.. " + e);
            return false;
        }
    }

    public static int generate(int val) {
        sleep(4000 + (int)(Math.random() * 10));
        //throw new RuntimeException("Exception occurred");
        return val;
    }

    public static Object report(Throwable throwable) {
        System.out.println("Error? " + throwable.getMessage());
        throw new RuntimeException("Oops");
    }

    public static CompletableFuture<Void> createTimeout(int ms){
        return CompletableFuture.supplyAsync(() ->{
            sleep(ms);
            throw new RuntimeException("Timeout reached");
        });
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> generate(1));
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> generate(2));
        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> generate(3));
        CompletableFuture<Integer> cf4 = CompletableFuture.supplyAsync(() -> generate(4));
        CompletableFuture<Integer> cf5 = CompletableFuture.supplyAsync(() -> generate(5));
        CompletableFuture<Void> cf6 = createTimeout(3000);

        // In this case, if any of the CFS responds within 3000 ms, then CF completes successfully. But if it doesn's respond within that, we complete the CF exceptionally
        // by throwing a runtime exception. For testing, same can be done by providing a sleep of more than 3 sec in generate function.
        // O/P for timeout: Error? java.lang.RuntimeException: Timeout reached
        CompletableFuture.anyOf(cf1, cf2, cf3, cf4, cf5,cf6)
                .exceptionally(CompletableTimeout::report)
                .thenApply(value -> (int) value * 10)
                .thenAccept(System.out::println);

        sleep(5000);
    }


}
