import java.util.concurrent.CompletableFuture;

public class CompletableChaining {
    public static int generate() {
        //System.out.println("Called:" + Thread.currentThread());
        return 2;  // For complete to complete
        //throw  new RuntimeException("Exception occurred in generated method");
    }

    public static void print(int value) {
        System.out.println(value + ".." + Thread.currentThread());
    }

    public static int printError(Throwable throwable) {
        System.out.println("Exception is :"+ throwable.getMessage());
        return 0;  //Exception to complete
        //throw new RuntimeException("Task ended abruptly"); // if we want to end execution of future task immediately - for exception to exception
    }

    public static int doLogging(Throwable throwable) {
        System.out.println("Something went wrong "+ throwable.getMessage());
        throw new RuntimeException("Task ended abruptly2");
    }


    public static void main(String[] args) {
        //***exception to exception
//        O/P:
//        Exception is :java.lang.RuntimeException: Exception occurred in generated method
//        Something went wrongjava.lang.RuntimeException: Task ended abruptly
//        CompletableFuture.supplyAsync(CompletableChaining::generate)
//                .exceptionally(CompletableChaining::printError)
//                .exceptionally(CompletableChaining::doLogging)
//                .thenAccept(CompletableChaining::print);

                //Exception to complete
//                o/p:
//                  Exception is :java.lang.RuntimeException: Exception occurred in generated method
//                  0..Thread[main,5,main]
        // Complete to complete scenario : O/P: 2..Thread[main,5,main]
                CompletableFuture.supplyAsync(CompletableChaining::generate)
                .exceptionally(CompletableChaining::printError)
                .exceptionally(CompletableChaining::doLogging)
                .thenAccept(CompletableChaining::print);
    }



    /*CompletableFuture ,we can think of it as two channels/tracks. Data/complete channel and exception channel. Data start flowing through
      data channel. If any stage has failures the flow goes to error channel. Now, from error channel
      it can again go to data channel or continue in error channel. So there are few possibilities:
       1.Complete to complete - happy path, no exception
       2.Complete to exception - when one stage in CF pipeline blows up meaning throws exception
       3.Exception to complete
       4.Exception to exception
       */


}
