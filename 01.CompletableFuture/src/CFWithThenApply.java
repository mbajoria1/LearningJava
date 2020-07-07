import java.util.concurrent.CompletableFuture;

public class CFWithThenApply {

    public static int process() {
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

    public static void main(String[] args) {
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(CFWithThenApply::process);
//        CompletableFuture<Integer> future2 = future.thenApply(CFWithThenApply::transform);
//        future2.thenAccept(CFWithThenApply::print);
        //Above code could also be written in below functional chaining way

//        CompletableFuture
//                .supplyAsync(CFWithThenApply::process)
//                .thenApply(CFWithThenApply::transform)
//                .thenAccept(CFWithThenApply::print);

        // Also could be done in async manner
        CompletableFuture
                .supplyAsync(CFWithThenApply::process)
                .thenApplyAsync(CFWithThenApply::transform)
                .thenAcceptAsync(CFWithThenApply::print);

    }


}
