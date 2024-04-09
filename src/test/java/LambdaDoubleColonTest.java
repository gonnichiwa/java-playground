import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class LambdaDoubleColonTest {
    @Test
    public void doubleColon() {
        List<String> list = Arrays.asList("aa","bb","cc","dd");
        String abc = "abc";
        list.forEach((item) -> System.out.println(item + abc));
        System.out.println("-----");

        // 더블콜론의 특징 : forEach()에서 넘어가는 파라미터 가짓수가 하나(item)임이 보장된다.
        list.forEach(System.out::println);
    }

    @Test
    public void doubleColonConstruct() {
        Function<String, Food> function1 = (String a) -> new Food(a);
        Food food = function1.apply("pizza");
        System.out.println(food.getName());

        Function<String, Food> function2 = Food::new;
        food = function2.apply("apple");
        System.out.println(food.getName());
    }
}

class Food {
    private String name;
    private String name2;
    public Food(String name){
        this.name = name;
    }
    public Food(){

    }
    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }
}
