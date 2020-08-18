import java.util.Arrays;
import java.util.List;

public class DelegatingUsingLambda {

    public static int transform(int x){
        System.out.println("Called");
        return x*2;
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);

        System.out.println(list.stream().
                filter(x -> x%2 != 0)
                .mapToInt(DelegatingUsingLambda::transform)
                .findFirst());
    }
}
