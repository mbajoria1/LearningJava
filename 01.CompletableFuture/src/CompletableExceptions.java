import java.util.concurrent.CompletableFuture;

public class CompletableExceptions {
    public static int generate() {
        //System.out.println("Called:" + Thread.currentThread());
        return 2;
    }

    public static void print(int value) {
        System.out.println(value + ".." + Thread.currentThread());
    }


    public static int printError(Throwable throwable) {
        System.out.println("Exception is :"+ throwable.getMessage());
        // return 0; - if we want to continue in future pipeline
        throw new RuntimeException("Task ended abruptly"); // if we want to end execution of future task immediately
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

//        CompletableFuture.supplyAsync(CompletableExceptions::generate)
//                .thenAccept(CompletableExceptions::print);

        completableFuture.exceptionally(CompletableExceptions::printError)
                .thenAccept(CompletableExceptions::print);

        //completableFuture.complete(6);   // Output: 6..Thread[main,5,main]

        // But if we want to blow up , there are two ways. 1. throw exception from generate method or call completeExceptionally

        completableFuture.completeExceptionally(new RuntimeException("The sky is pink"));  // Output: Exception is :The sky is pink

        System.out.println(completableFuture.isDone());
        System.out.println(completableFuture.isCancelled());
        System.out.println(completableFuture.isCompletedExceptionally());

        // Output of above 3 statements:
//        true
//        false
//        true

    }
}
