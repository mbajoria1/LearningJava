import java.util.concurrent.CompletableFuture;

public class CompletableAnyof {

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
        double x = Math.random() * 100;
        sleep(2000);
        //throw new RuntimeException("Exception occurred");
        return (int) (val * x);
    }

    public static String generate(String s) {
        return "My name is " + s;
    }

    public static double generate() {
        sleep(1000);
        return Math.random() * 100;
    }

    public static Object report(Throwable throwable) {
        System.out.println("Error? " + throwable.getMessage());
        throw new RuntimeException("Oops");
    }

    public static void main(String[] args) {
//        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> generate(1));
//        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> generate(2));
//        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> generate(3));
//        CompletableFuture<Integer> cf4 = CompletableFuture.supplyAsync(() -> generate(4));
//        CompletableFuture<Integer> cf5 = CompletableFuture.supplyAsync(() -> generate(5));
//        // Note, in case of anyOf in can take CFs of multiple times like double, integer, string etc, below is one example
//        // and anyOf retuns an object, so we cant apply direct operations on returned value without explicit type casting.
//        CompletableFuture.anyOf(cf1, cf2, cf3, cf4, cf5)
//                .thenApply(value -> (int) value * 10)
//                .thenAccept(System.out::println);


//        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> generate(1));
//        CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> generate(2));
//        CompletableFuture<Integer> c3 = CompletableFuture.supplyAsync(() -> generate(3));
//        CompletableFuture<Double> c4 = CompletableFuture.supplyAsync(() -> generate());
//        CompletableFuture<String> c5 = CompletableFuture.supplyAsync(() -> generate("Madhuri"));

//        CompletableFuture.anyOf(c1, c2, c3, c4, c5)
//                .thenAccept(System.out::println);

//        sleep(3000);

        // Exception in anyOf

        CompletableFuture<Integer> cf6 = CompletableFuture.supplyAsync(() -> generate(1));
        CompletableFuture<Integer> cf7 = CompletableFuture.supplyAsync(() -> generate(2));
        CompletableFuture<Integer> cf8 = CompletableFuture.supplyAsync(() -> generate(3));
        CompletableFuture<Integer> cf9 = CompletableFuture.supplyAsync(() -> generate(4));
        CompletableFuture<Integer> cf10 = new CompletableFuture();

//        CompletableFuture.anyOf(cf6, cf7, cf8, cf9,cf10)
//                .exceptionally(CompletableAnyof::report)
//                .thenApply(value -> (int) value * 10)
//                .thenAccept(System.out::println);

        //if we cancel one of the CFs

        CompletableFuture.anyOf(cf6, cf7, cf8, cf9,cf10)
                .exceptionally(CompletableAnyof::report)
                .thenAccept(System.out::println);
        sleep(500);
        cf10.cancel(true);

        // O/P: Error? java.util.concurrent.CancellationException
    }
}
