import java.util.concurrent.CompletableFuture;

public class CompletableThenCombine {

    public static void print(double value) {
        System.out.println(value + ".." + Thread.currentThread());
    }

    public static void main(String[] args) {
        CompletableFuture<Double> google = getFuture("google",200);
        CompletableFuture<Double> amzn = getFuture("amazon",300);

        google.thenCombine(amzn , Double::sum)
                .thenAccept(CompletableThenCombine::print);
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

    public static CompletableFuture<Double> getFuture(String stockName,int noOfShares){
        return CompletableFuture.supplyAsync(() -> getPrice(stockName) * noOfShares);
    }
}
