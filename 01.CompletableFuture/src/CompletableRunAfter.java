import com.sun.deploy.security.ruleset.RunRule;

import java.util.concurrent.CompletableFuture;

public class CompletableRunAfter {

    public static int generate(int number) {
        if (number == 3) {
            sleep(2000);
            // Slower one throwing exception
            throw new RuntimeException("Oops!!");
        }
        // Just to see how exceptions are handled

        if(number == 2){
           throw new RuntimeException("Oops!!");
        }

        return number;
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> generate(2));
        CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> generate(3));

        // applyToEither can be used to provide some transformation to the future object which is returned first.
//        c1.applyToEither(c2, either -> either * 1.0)
//                .thenAccept(System.out::println);

        // runAfterEither or runAfterBoth can be used when we dont bother about what happened to the futute operations, we just want to perform
        // some operations once one of them or both in our case completes execution.
        //c1.runAfterBoth(c2, () -> System.out.println("Finished"));
        //c1.runAfterEither(c2, () -> System.out.println("Finished"));

        // handling exceptions in runAfter

        //c1.runAfterEither(c2, () -> System.out.println("Finished"));  // will not produce any result as we are not handling exception and first one
        // completes exceptionally
        // Lets change the code to throw exception for 2nd one exceptionally and 1st one successfully, then we would notice program completes successfully
        // as runAfterEither takes the first result which comes in, if 1st one is success it completes successfully and if 1st one fails or blows up then
        // runAfter also doesn't complete successfully. What happens to the 2nd one is simply ignored.
        // But for runAfterBoth - the combined result matters. Success or failure.

        // Lets put exceptionally to handle exception. For runAfterEither & runAfterBoth the result will be kind of same, of 1st one to complete throws an
        // exception then runAfterEither will complete exceptionally else successfully.
        // For runAfterBoth if either of them throws exception, it will complete exceptionally.

//        c1.runAfterEither(c2, () -> System.out.println("Finished"))
//        .exceptionally(CompletableRunAfter::report);


        c1.runAfterBoth(c2, () -> System.out.println("Finished"))
                .exceptionally(CompletableRunAfter::report);

        sleep(3000); // for the main thread to hold

        //O/P: Error: java.lang.RuntimeException: Oops!!
    }

    public static Void report(Throwable throwable) {
        System.out.println("Error: "+ throwable.getMessage());
        throw new RuntimeException("Terminated");
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