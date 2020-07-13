import java.util.concurrent.CompletableFuture;

public class CFAcceptApplyEither {

    public static int getPrice(String stockName){
        int price = 0;
        switch (stockName) {
            case "google":
                sleep(1000);
                price= 1000;
                break;
            case "amazon":
                price= 2000;
                break;
            case "default" :
                price = 500;

        }
        return price;
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> getPrice("google"));
        CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> getPrice("amazon"));

        //c1.acceptEither(c2, System.out::println);

        c1.applyToEither(c2, price -> price * 1.0)
        .thenAccept(System.out::println);

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
