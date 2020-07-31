import java.util.Arrays;
import java.util.List;

public class IterativePattern {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12);
        System.out.println(list.stream()
                .filter(x -> x%2 ==0)
                .mapToInt(x -> x*2)
                .sum());


    }
}
