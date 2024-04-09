import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LambdaDoubleColonTest {
    @Test
    public void doubleColon() {
        List<String> list = Arrays.asList("aa","bb","cc","dd");
        String abc = "abc";
        list.forEach((item) -> System.out.println(item + abc));
        System.out.println("-----");

        // 더블콜론의 특징 : forEach()에서 넘어가는 파라미터 가짓수가 하나(item)임이 보장된다.
        // 메소드 레퍼런스
        list.forEach(System.out::println);
    }

    @Test
    public void doubleColonConstruct() {
        Function<String, Food> function1 = (String a) -> new Food(a+"123");
        Food food = function1.apply("pizza");
        assertThat(food.getName(), is("pizza123"));

        Function<String, Food> function2 = Food::new;
        food = function2.apply("apple");
        assertThat(food.getName(), is("apple"));

        Function<Persona, Food> function3 = person -> new Food(person.wantToEat);
        Food food3 = function3.apply(new Persona("name", 22, "apple"));
        assertThat(food3.getName(), is("apple"));

        Function<Persona, Food> function4 = (person) -> new Food(person.wantToEat, person.name);
        Food food4 = function4.apply(new Persona("jj", 22, "cereal"));
        assertThat(food4.getName(), is("cereal"));
        assertThat(food4.getWhosName(), is("jj"));
    }
}
class Persona {
    String name;
    int age;
    String wantToEat;

    public Persona(String name, int age, String wantToEat) {
        this.name = name;
        this.age = age;
        this.wantToEat = wantToEat;
    }
}

class Food {
    private final String name;
    private String whosName = null;

    public Food(String name, String whosName){
        this.name = name;
        this.whosName = whosName;
    }
    public Food(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getWhosName() {
        if(whosName != null) return whosName;
        throw new NullPointerException("whosName is Null");
    }
}
