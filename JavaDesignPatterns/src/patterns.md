## Iterator pattern:

Using java8 `stream` & lambda functions we can use lambda pipeline to write better understandable & readable code
and also can use internal iterators than using for or foreach which are internal iterators to loop through a collection. 
```
  List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12);
        System.out.println(list.stream()
                .filter(x -> x%2 ==0)
                .mapToInt(x -> x*2)
                .sum());

```
<br/>

## Lightweight Strategy Pattern:
Strategy pattern is when you have for example an interface to perform most of your common functionality and you implement that in different classes 
to change little variable part of your functions.

With lambda strategy pattern can be implemented even with lightweight code. Below is a small code example. Here we are passing a stretegy to 
the function to help in computation of total numbers.
```
 public static int findTotalLambda(List<Integer> list, Predicate<Integer> selector){
        return list.stream()
                .filter(selector)
                .mapToInt(x-> x)
                .sum();
  }
  
   System.out.println(" Total of all numbers: " +findTotalLambda(list, x->true));
   System.out.println(" Total of even numbers: " +findTotalLambda(list, x-> x%2 == 0 ));
   System.out.println(" Total of odd numbers: " +findTotalLambda(list, x-> x%2 != 0 ));
    
```
<br/> 

## Decorating using lambda:
<br/>

## Fluent interfaces using lambda:
<br/>

## Execute Around Method pattern:      
<br/>