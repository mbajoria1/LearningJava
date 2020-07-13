import java.util.concurrent.CompletableFuture;

public class CompletableCancel {

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

    public static void report(CompletableFuture<Integer> completableFuture){
        System.out.println("Done? " +completableFuture.isDone());
        System.out.println("Cancelled? " +completableFuture.isCancelled());
    }

    public static void main(String[] args) {
//        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
//        report(completableFuture);
//        process(completableFuture);    // building a pipeline by passing future object to a different method
//        report(completableFuture);
//        completableFuture.complete(5);
//        report(completableFuture);
//        System.out.println("Done");

        // O/P:

//        Done? false
//        Cancelled? false
//        Done? false
//        Cancelled? false
//        Transform called in thread:Thread[main,5,main]
//        10..Thread[main,5,main]
//        Done? true
//        Cancelled? false
//        Done


        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        report(completableFuture);
        process(completableFuture);    // building a pipeline by passing future object to a different method
        report(completableFuture);
        completableFuture.cancel(true);
        completableFuture.complete(5);
        report(completableFuture);
        System.out.println("Done");
//
//        O/P:
//        Done? false
//        Cancelled? false
//        Done? false
//        Cancelled? false
//        Done? true
//        Cancelled? true
//        Done
// **** Notice in above o/p process method hasn't executed at call when we called completableFuture.complete(5) as future has already been cancelled ,
// **** complete() call is ignored. Again if we call cancel() after complete() statement , cancel() would be ignored as task has already been completed
// **** and there is nothing to cancel. See below example

        System.out.println("");
        CompletableFuture<Integer> completableFuture2 = new CompletableFuture<>();
        report(completableFuture2);
        process(completableFuture2);    // building a pipeline by passing future object to a different method
        report(completableFuture2);
        completableFuture2.complete(5);
        completableFuture2.cancel(true);
        report(completableFuture2);
        System.out.println("Done");

//        O/P:
//        Done? false
//        Cancelled? false
//        Done? false
//        Cancelled? false
//        Transform called in thread:Thread[main,5,main]
//        10..Thread[main,5,main]
//        Done? true
//        Cancelled? false
//        Done


    }
}
