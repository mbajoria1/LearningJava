import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureIssues {
    public static int process(int Max,int index){
        System.out.println("Starting task number..."+index);
        int ms = (Max-index)*1000;
        System.out.println("index.. "+index +" " +ms);
        sleep(ms);
        System.out.println("Finishing task number..."+index);
        throw new RuntimeException("Oops.."+index);
        //return index;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        int max = 10;
        List<Future<Integer>> results = new ArrayList<>();

        for (int i = 1; i < max; i++) {
            int finalI = i;
            //Future<Integer> result = executorService.submit(() -> process(max,finalI));
            results.add(executorService.submit(() -> process(max,finalI)));
            //System.out.println(results.get()); // As result.get() is a blocking call, future task scheduled will wait until it's previous one has completed.
            //So instead will use list of future, but even after using list when we try to get result from a scheduled task, if previous one hasn't yet completed
            //yet it will still be blocking call
        }
        System.out.println("All tasks scheduled...");

        results.forEach(x -> {
            try {
                System.out.println("Task result " +x.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

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
