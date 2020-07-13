import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableThenCompose {
    // map : thenApply is used when a function takes a value and returns a value
    // flatmap : thenCompose is used when a function takes a value and returns a completable future

    public static void main(String[] args) {
        getStockName(1)
                .thenCompose(stock -> getFuture(stock,200)) // as getFuture takes values but returns CF.
                .thenAccept(System.out::println);
    }

    public static double getPrice(String stockName){
        double price = 0.0;
        switch (stockName) {
            case "google":
                price= 1000;
                break;
            case "amazon":
                price= 2000;
                break;
        }
        return price;
    }

    public static CompletableFuture<Double> getFuture(String stockName, int noOfShares){
        return CompletableFuture.supplyAsync(() -> getPrice(stockName) * noOfShares);
    }

    public static CompletableFuture<String> getStockName(int index){
        List<String> stockNames = Arrays.asList("google","amazon");
        return CompletableFuture.supplyAsync(() -> stockNames.get(index));
    }

}
