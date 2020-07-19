import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class CompletableAllof {
    public static int generate(int val) {
        sleep(1000 + (int)(Math.random() * 10));
        //if(val == 3)
        //    throw new RuntimeException("Exception occurred");
        return val;
    }

    public static Void report(Throwable throwable) {
        System.out.println("Error? " + throwable.getMessage());
        throw new RuntimeException("Oops");
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

    public static boolean isPrime(int number){
        return number > 1 && IntStream.range(2,number).noneMatch( i -> number % i == 0);
    }

    public static CompletableFuture<String> countPrime(int number){
        if(number < 1){
            throw new RuntimeException("Invalid Input...");
        }

        return CompletableFuture.supplyAsync(
                () -> IntStream.range(2,number).filter(x -> isPrime(x)).count()).thenApply(count -> number+"---"+count);
    }


    public static void processRequest(List<CompletableFuture<String>> futureList){
        futureList.stream().forEach(future -> future.thenAccept(System.out::println));
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> generate(1));
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> generate(2));
        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> generate(3));
        CompletableFuture<Integer> cf4 = CompletableFuture.supplyAsync(() -> generate(4));
        CompletableFuture<Integer> cf5 = CompletableFuture.supplyAsync(() -> generate(5));
        CompletableFuture<Void>    cf6 = new CompletableFuture<>();

        CompletableFuture.allOf(cf1, cf2, cf3, cf4, cf5,cf6)
                .exceptionally(CompletableAllof::report)
                .thenRun(() -> System.out.println("DONE"));

        cf6.cancel(true);

        sleep(10000);

        // O/P: Error? java.util.concurrent.CancellationException

        // Using allOf

        CompletableFuture<String> prime1 = countPrime(100);
        CompletableFuture<String> prime2 = countPrime(200);
        CompletableFuture<String> prime3 = countPrime(300);
        CompletableFuture<String> prime4 = countPrime(-500);
        CompletableFuture.anyOf(prime1,prime2,prime3,prime4)
                .exceptionally(CompletableAllof::report)
                .thenRun(() -> processRequest(Arrays.asList(prime1,prime2,prime3,prime4)));

        sleep(5000);

//        O/P:
//        100---25
//        200---46
//        300---62

        // if we add prime4 then O/P will be as below:

        /*Exception in thread "main" java.lang.RuntimeException: Invalid Input...
        at CompletableAllof.countPrime(CompletableAllof.java:36)
        at CompletableAllof.main(CompletableAllof.java:71)*/

    }
}
