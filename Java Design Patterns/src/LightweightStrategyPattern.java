import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

// High-Order functions which
// 1. Functions which return functions
// 2. Take function as input parameter
// 3. Create function

public class LightweightPattern {
    public static int findTotal(List<Integer> list){
        int result = 0;
        for(int x: list){
            result+=x;
        }
        return result;
    }
    public static int findEvenTotal(List<Integer> list){
        int result = 0;
        for(int x: list){
            if(x%2==0)
                result+=x;
        }
        return result;
    }

    //With Strategy pattern & lambda above code could also be written in a lot better way

    public static int findTotalLambda(List<Integer> list, Predicate<Integer> selector){
        return list.stream()
                .filter(selector)
                .mapToInt(x-> x)
                .sum();
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        System.out.println(findTotal(list));
        System.out.println(findEvenTotal(list));

        // So in below code, we are passing a predicate to findTotalLambda function and this way some part of the logic
        // is handled by function and some is passed by calling code.
        System.out.println(" Total of all numbers: " +findTotalLambda(list, x->true));
        System.out.println(" Total of even numbers: " +findTotalLambda(list, x-> x%2 == 0 ));
        System.out.println(" Total of odd numbers: " +findTotalLambda(list, x-> x%2 != 0 ));
    }
}
