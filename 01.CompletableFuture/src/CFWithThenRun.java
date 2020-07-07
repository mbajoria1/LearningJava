import java.util.concurrent.CompletableFuture;

public class CFWithThenRun {
// thenRun can be used in different usecases such as at the end of an process execution for logging purposes.
// To print a message in console etc. Below is a simple working example.

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
        CompletableFuture
                .supplyAsync(CFWithThenApply::process)
                .thenApplyAsync(CFWithThenApply::transform)
                .thenAcceptAsync(CFWithThenApply::print)
                .thenRun(() -> System.out.println("We are done here..."));
    }
}
