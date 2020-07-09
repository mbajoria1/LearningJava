import java.sql.SQLOutput;
import java.util.concurrent.CompletableFuture;

public class CompletableComplete {

    public static int generate() {
        System.out.println("Called:" + Thread.currentThread());
        return 2;
    }

    public static void print(int value) {
        System.out.println(value + ".." + Thread.currentThread());
    }

    public static int transform(int value){
        System.out.println("Transform called in thread:"+Thread.currentThread());
        return value *2;
    }

    public static void process(CompletableFuture<Integer> completableFuture){
        completableFuture
                .thenApply(CompletableComplete::transform)
                .thenAccept(CompletableComplete::print);
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        System.out.println(completableFuture);
        process(completableFuture);    // building a pipeline by passing future object to a different method
        System.out.println(completableFuture);
        System.out.println("Built pipeline");
        completableFuture.complete(5);
        System.out.println(completableFuture);
        System.out.println("Done");

        // O/P:
//        java.util.concurrent.CompletableFuture@14ae5a5[Not completed]
//        java.util.concurrent.CompletableFuture@14ae5a5[Not completed, 1 dependents]
//        Built pipeline
//        Transform called in thread:Thread[main,5,main]
//        10..Thread[main,5,main]
//        java.util.concurrent.CompletableFuture@14ae5a5[Completed normally]
//        Done

    }
}
